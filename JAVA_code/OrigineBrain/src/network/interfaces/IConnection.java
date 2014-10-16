package network.interfaces;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Représente une connexion distante
 * @author Jeremy
 */
public interface IConnection extends Closeable {
	
	/**
	 * Renvoit le flux en entrée de la connection
	 * @return
	 * @throws IOException
	 */
	public InputStream getInputStream() throws IOException;
	
	/**
	 * Renvoit le flux en sortie de la connection
	 * @return
	 * @throws IOException
	 */
	public OutputStream getOutputStream() throws IOException;
	
	/**
	 * Test si la connection est fermée
	 * @return
	 */
	public boolean isClosed();
}
