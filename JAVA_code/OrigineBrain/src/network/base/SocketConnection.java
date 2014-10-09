package network.base;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import network.interfaces.IConnection;

/**
 * Classe permettant le typage d'une socket avec le reste
 * des classes du pakage
 * ne fait qu'un transfert de méthodes (socket implémente déjà IConnection,
 * mais le typage empêche de s'en servir directement ...)
 * 
 * Ajoute au passage des BufferedStream
 * 
 * @author Jeremy
 *
 */
public class SocketConnection implements IConnection {

	private Socket s;
	private String toStr = null;
	
	public SocketConnection(Socket s) {
		this.s = s;
	}
	
	@Override
	public void close() throws IOException {
		s.close();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new BufferedInputStream(s.getInputStream());
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return new BufferedOutputStream(s.getOutputStream());
	}

	@Override
	public boolean isClosed() {
		return s.isClosed();
	}

	@Override
	public String toString() {
		if(toStr==null)
			toStr = s.getInetAddress().getHostName()+"@"+s.getInetAddress()+":"+s.getPort(); 
		return toStr;
	}
}
