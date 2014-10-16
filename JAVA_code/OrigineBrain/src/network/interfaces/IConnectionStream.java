package network.interfaces;

import java.io.IOException;

/**
 * Rajoute à une connection la possibilité d'envoyer / recevoir des données complexes
 * @author Jeremy
 *
 */
public interface IConnectionStream extends IConnection {
	
	/**
	 * Send an object of unknow type
	 * Use reflection to detect type
	 * @param o
	 * @throws IOException if object type don't handled, or connection closed, ...
	 */
	public void send(Object o) throws IOException;
	
	/**
	 * Try receiving an object of the type given
	 * If the type asked can't be handle, throws UnsupportedOperationException
	 * @param type class of the object to be received
	 * @return qualified object (if no exception)
	 * @throws IOException if reading error (object type not handled, can't instancite the type asked, connection closed, ...)
	 */
	public Object receiveObject(Class<?> type)  throws IOException;
	
}
