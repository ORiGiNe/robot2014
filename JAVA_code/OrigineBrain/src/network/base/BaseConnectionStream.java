package network.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidParameterException;

import network.interfaces.IConnection;
import network.interfaces.IConnectionStream;
import network.interfaces.IDto;

/**
 * Implémentation de la gestion des types de bases : string, int et tableaux
 * à l'aide de sendByte et receiveByte (seules ces fonctions envoient éffectivement des données)
 * 
 * Permet de choisir l'ordre des bytes envoyés via setEndianess
 * 
 * gère la synchronisation : les fonctions sendXxx et receiveXxx
 * posent un lock sur les streams d'entrée/ sortie
 * 
 * Les stream sont privés et forcent une éventuelle sous classe à utiliser
 * les méthodes de la classe mère, donc à garantir ses locks
 * 
 * Attention au blocage avec close() :
 * si un thread est en cours de lecture, close sera bloqué
 * Ferme également la connection sous-jacente
 * 
 * La synchronisation est peut être à vérifier
 * 
 * Ne permet pas d'envoyer des objets nuls
 * 
 * @author Jeremy
 */
public class BaseConnectionStream implements IConnectionStream {
	
	private ByteBuffer byteBuffer = ByteBuffer.allocate(4);
	private ByteOrder order = ByteOrder.BIG_ENDIAN;
	
	// Stream privés : une classe fille ne pourra envoyer des données qu'avec super.send...
	// Ainsi : on garantie que la classe fille conserve l'utiliastion des locks mise en place
	private InputStream in;
	private OutputStream out;
	private IConnection connection;
	
	public BaseConnectionStream() {
		this.in = null;
		this.out = null;
		this.connection = null;
	}
	
	/**
	 * Créé un ConnectionStream depuis une socket "convertie" en IConnection
	 * @param s
	 * @throws IOException
	 */
	public BaseConnectionStream(Socket s) throws IOException {
		this(new SocketConnection(s));
	}
	
	/**
	 * Créé un ConnectionStream branché sur la connection fournie
	 * @see setConnection
	 * @param connection
	 * @throws IOException si la connection fournie la lève lors de l'obtention d'un de ses flux @see setConnection
	 */
	public BaseConnectionStream(IConnection connection) throws IOException {
		setConnection(connection);
	}
	
	/**
	 * change the current connection used
	 * throws InvalidParameterException if the connexion is closed or gives null streams
	 * WARNING : does not close the underlying connection
	 * use close() before using this function if needed
	 * @param connection
	 * @throws IOException si la connection fournie la lève lors de l'obtention d'un de ses flux
	 */
	public void setConnection(IConnection connection) throws IOException {
		this.connection = connection;
		this.in = connection.getInputStream();
		this.out = connection.getOutputStream();
		if(this.connection == null || this.in == null || this.out == null)
		{
			throw new InvalidParameterException("The connection provided is closed or provided null streams");
		}
	}
		
	protected void sendByte(byte x) throws IOException {
		synchronized (out) {
			out.write(x);
		}
	}
	
	protected byte receiveByte() throws IOException {
		if(in==null) {
			throw new IOException(this.getClass().getCanonicalName()+" : input stream is null");
		}
		synchronized (in) {
			byte b = (byte) (in.read() & 0xff);
			return b;
		}
		
	}
	
	protected void sendInt(int x) throws IOException {
		synchronized (out) {
			byteBuffer.order(order);
			byteBuffer.rewind();
			byteBuffer.putInt(x);
			byte[] convert = byteBuffer.array();
			for(int j=0; j<4; ++j) {
				this.sendByte(convert[j]);
			}
		}
	}

	protected int receiveInt() throws IOException {
		synchronized (in) {
			byte[] tab = new byte[4];
			tab[0] = this.receiveByte();
			tab[1] = this.receiveByte();
			tab[2] = this.receiveByte();
			tab[3] = this.receiveByte();
			byteBuffer.order(order);
			byteBuffer.rewind();
			byteBuffer.put(tab);
			byteBuffer.rewind();
			return byteBuffer.getInt();
		}
	}

	/**
	 * Envoi la taille du tableau (1 int) puis chaque éléments
	 * @param table
	 * @throws IOException
	 */
	protected void sendIntArray(int[] table) throws IOException {
		synchronized (out) {
			this.sendInt(table.length);
			for(int i=0; i<table.length; ++i) {
				this.sendInt(table[i]);
			}
		}
	}

	/**
	 * Lit un int pour la taille du tableau (nombre d'éléments)
	 * puis lit chaque élements
	 * @return
	 * @throws IOException
	 */
	protected int[] receiveIntArray() throws IOException {
		synchronized (in) {
			int length = this.receiveInt();
			int[] tab = new int[length];
			for(int i=0; i<length; ++i) {
				tab[i] = this.receiveInt();
			}
			return tab;
		}
	}

	/**
	 * Envoie la taille du tableau (1 int) suivit de chaque byte du tableau
	 * @param table
	 * @throws IOException
	 */
	protected void sendByteArray(byte[] table) throws IOException {
		synchronized (out) {
			this.sendInt(table.length);
			for(int i=0; i<table.length; ++i) {
				this.sendByte(table[i]);
			}
		}
	}

	/**
	 * lit 1 int pour la taille du tableau
	 * puis lit chaque élement
	 * @return
	 * @throws IOException
	 */
	protected byte[] receiveByteArray() throws IOException {
		synchronized (in) {
			int length = receiveInt();
			byte[] tab = new byte[length];
			for(int i=0; i<length; ++i) {
				tab[i] = this.receiveByte();
			}
			return tab;
		}
	}

	/**
	 * Convertit la chaine en byte[] et utilie @see sendByteArray
	 * @param s
	 * @throws IOException
	 */
	protected void sendString(String s) throws IOException {
		this.sendByteArray(s.getBytes());
	}

	/**
	 * Recoit un byte[] (@see receiveByteArray) et créer une String avec 
	 * @return
	 * @throws IOException
	 */
	protected String receiveString() throws IOException {
		byte[] tab = this.receiveByteArray();
		return new String(tab);
	}

	/**
	 * Permet d'envoyer des objets de type :
	 * int, int[], byte, byte[], string, IDto
	 * doivent être NON NULL !
	 */
	@Override
	public synchronized void send(Object o) throws IOException {
		if(out==null) {
			throw new IOException(this.getClass().getCanonicalName()+" : output stream is null");
		}
		if(this.isClosed()) {
			throw new IOException(this.getClass().getCanonicalName()+" : this stream is disconnected");
		}
		
		if(o==null) {
			throw new IOException("object provided is null");
		}
		Class<?> cl = o.getClass();
		if(cl.isAssignableFrom(Integer.class) || cl.isAssignableFrom(int.class)) {
			sendInt((int) o);
		} else if(cl.isAssignableFrom(int[].class)) {
			sendIntArray((int[]) o);
		} else if(cl.isAssignableFrom(Byte.class) || cl.isAssignableFrom(byte.class)) {
			sendByte((byte) o);
		} else if(cl.isAssignableFrom(byte[].class)) {
			sendByteArray((byte[]) o);
		} else if(cl.isAssignableFrom(String.class)) {
			sendString((String) o);
		} else if(IDto.class.isAssignableFrom(cl)) {
			((IDto) o).writeIn(this);
		} else {
			throw new IOException(this.getClass().getCanonicalName()+".send(Object) can't send an object of type \""+o.getClass().getCanonicalName()+"\"");
		}
		synchronized (out) {
			out.flush(); // envois les éventuelles données restantes (bufferStream)
		}
	}

	@Override
	public Object receiveObject(Class<?> type) throws IOException {
		if(in==null) {
			throw new IOException(this.getClass().getCanonicalName()+" : input stream is null");
		}
		if(this.isClosed()) {
			throw new IOException(this.getClass().getCanonicalName()+" : this stream is disconnected");
		}
		
		if(type.isAssignableFrom(Integer.class) || type.isAssignableFrom(int.class)) {
			return receiveInt();
		} else if(type.isAssignableFrom(int[].class)) {
			return receiveIntArray();
		} else if(type.isAssignableFrom(Byte.class) || type.isAssignableFrom(byte.class)) {
			return receiveByte();
		} else if(type.isAssignableFrom(byte[].class)) {
			return receiveByteArray();
		} else if(type.isAssignableFrom(String.class)) {
			return receiveString();
		} else if(IDto.class.isAssignableFrom(type)) {
			try {
				Object o;
				o = type.newInstance();
				((IDto) o).readFrom(this);
				return o;
			} catch (IOException | InstantiationException | IllegalAccessException e) {
				throw new IOException("Unable to receive object of type "+type.getCanonicalName(), e);
			}
		} else {
			throw new IOException(this.getClass().getCanonicalName()+".receive(Object) can't receive an object of type \""+type.getCanonicalName()+"\"");
		}
	}

	public final void setEndianess(ByteOrder b) {
		order = b;
	}

	public final ByteOrder getEndianess() {
		return order;
	}
	
	/**
	 * Cette implémentation ne prends pas de lock
	 * sur in / out de manière à fermer à coup sûr quitte à faire planter un thread en lecture.
	 * Elle prends par contre le lock sur le ConnectionStream pour éviter
	 * 2 fermetures simultanées, ou même de couper un envois en cours
	 */
	@Override
	public synchronized final void close() {
		if(!isClosed()) {
			try {
				if(out!=null) {
					out.flush();
					out.close();
				}
				if(in!=null)
					in.close();
				if(connection!=null)
					connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public InputStream getInputStream() throws IOException {
		if(this.in==null) {
			throw new IOException(this.getClass().getCanonicalName()+" : inputStream is null");
		}
		return this.in;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		if(this.in==null) {
			throw new IOException(this.getClass().getCanonicalName()+" : outputStream is null");
		}
		return this.out;
	}

	@Override
	public boolean isClosed() {
		if(connection!=null)
			return connection.isClosed();
		return true;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"["+this.getEndianess()+" / "+connection.toString()+"]";
	}

}
