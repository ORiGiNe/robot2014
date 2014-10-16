package network.interfaces;

import java.io.IOException;


/**
 * Représente un message (Data Transfert Object)
 * 
 * Le rôle du dto est de définir QUOI envoyer
 * Le ConnectionStream définit COMMENT envoyer (comment est transmis un tableau, etc ...)
 * La connexion choisit à qui envoyer (réseau, port usb, ...)
 * 
 * Un Dto se transmet via une ConnectionStream, branché sur une connection
 * 
 * Le code typique serait :
 * 
 * Conection conn = new Connection(...);
 * conn.connect(target);
 * ConnectionStream stream = new ConnectionStream(conn);
 * stream.send(myDto);
 * 
 * Une fois la connection effectuée, on ne manipule plus que le stream
 * sa fermeture entrainera celle de la connection
 * 
 * @author Jeremy
 *
 */
public interface IDto {
	
	/**
	 * Ecrit les données du Dto dans le flux
	 * @param stream
	 * @throws IOException en cas d'erreur lors de l'envoi
	 */
	public void writeIn(IConnectionStream stream) throws IOException;
	
	/**
	 * Reçoit les données du Dto depuis le flux
	 * @param stream
	 * @throws IOException en cas d'erreur lors de l'envoi
	 */
	public void readFrom(IConnectionStream stream) throws IOException;
}
