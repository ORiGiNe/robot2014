package network.interfaces;

import java.io.IOException;


/**
 * Repr�sente un message (Data Transfert Object)
 * 
 * Le r�le du dto est de d�finir QUOI envoyer
 * Le ConnectionStream d�finit COMMENT envoyer (comment est transmis un tableau, etc ...)
 * La connexion choisit � qui envoyer (r�seau, port usb, ...)
 * 
 * Un Dto se transmet via une ConnectionStream, branch� sur une connection
 * 
 * Le code typique serait :
 * 
 * Conection conn = new Connection(...);
 * conn.connect(target);
 * ConnectionStream stream = new ConnectionStream(conn);
 * stream.send(myDto);
 * 
 * Une fois la connection effectu�e, on ne manipule plus que le stream
 * sa fermeture entrainera celle de la connection
 * 
 * @author Jeremy
 *
 */
public interface IDto {
	
	/**
	 * Ecrit les donn�es du Dto dans le flux
	 * @param stream
	 * @throws IOException en cas d'erreur lors de l'envoi
	 */
	public void writeIn(IConnectionStream stream) throws IOException;
	
	/**
	 * Re�oit les donn�es du Dto depuis le flux
	 * @param stream
	 * @throws IOException en cas d'erreur lors de l'envoi
	 */
	public void readFrom(IConnectionStream stream) throws IOException;
}
