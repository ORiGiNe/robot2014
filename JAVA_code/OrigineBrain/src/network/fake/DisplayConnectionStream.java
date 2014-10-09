package network.fake;

import java.io.IOException;

import network.base.BaseConnectionStream;
import network.interfaces.IConnection;
import tools.Tools;

/**
 * Affiche les objets reçus en envoyés, ainsi que le nombre d'octets transmis (reçu) pour chacun
 * @author Jeremy
 *
 */
public class DisplayConnectionStream extends BaseConnectionStream {
	
	private int nbWritten;
	private int nbRead;
	
	public DisplayConnectionStream(IConnection conn) throws IOException {
		super(conn);
	}
	
	@Override
	protected void sendByte(byte x) throws IOException {
		super.sendByte(x);
		nbWritten ++;
	}

	@Override
	protected byte receiveByte() throws IOException {
		nbRead ++;
		return super.receiveByte();
	}

	@Override
	public void send(Object o) throws IOException {
		int oldcount = nbWritten;
		super.send(o);
		System.out.println("==> "+this.getClass().getSimpleName()+" has sent a "+o.getClass().getSimpleName()+" : "+Tools.reflectDisplay(o)
								+" ("+(nbWritten-oldcount)+" bytes)");
	}
	
	@Override
	public Object receiveObject(Class<?> type) throws IOException {
		int oldcount = nbRead;
		Object ret = super.receiveObject(type);
		System.out.println("<== "+this.getClass().getSimpleName()+" has received a "+ret.getClass().getSimpleName()+" : "+Tools.reflectDisplay(ret)
				+" ("+(oldcount-nbRead)+" bytes)");
		return ret;
	}
	
	@Override
	public String toString() {
		return "FakeConnectionStream ["+this.getEndianess()+"]";
	}
}
