package drivers.base;

import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * Classe de base pour les drivers de cartes.
 * Fournie des pins<br />
 * Les fonctions d'écritures et de lecture de données de la carte
 * sont en portée "protected" => les classes du même pakage y ont accès, en particulier les pins<br />
 * L'usage des classes Pin est donc obligatoire pour communiquer avec la carte.<br />
 * @author Jeremy
 * @see AnalogInputPin
 * @see AnalogOutputPin
 * @see InputPin
 * @see OutputPin
 *
 */
public abstract class AbstractCardDriver {
	
	/**
	 * Read a digital entry from the card
	 * @param pinId the pin to read
	 * @return the actual level of the pin
	 * @throws IOException in case of any error
	 * @see IoLevel
	 * @see InputPin#digitalRead()
	 */
	protected abstract IoLevel digitalRead(int pinId) throws IOException;
	
	/**
	 * Read an analogic value from the card
	 * the returned value is between 0 and 255 included
	 * @param pinId the pin Id to read
	 * @return the value read ( 0<= return <= 255)
	 * @throws IOException in case of any error
	 * @see AnalogInputPin#analogRead()
	 */
	protected abstract int analogRead(int pinId) throws IOException;
	
	/**
	 * Change the pin level
	 * @param pinId the pin Id
	 * @param level the new level
	 * @throws IOException in case of any error
	 * @see IoLevel
	 * @see OutputPin#digitalWrite(IoLevel)
	 */
	protected abstract void digitalWrite(int pinId, IoLevel level) throws IOException;
	
	/**
	 * write an analogic value between 0 and 255
	 * @param pinId the pind id where to write
	 * @param level the new value, ( 0 <= level <= 255 )
	 * @throws IOException in case of any error
	 * @see AnalogOutputPin#analogWrite(int)
	 */
	protected abstract void analogWrite(int pinId, int level) throws IOException;
	
	/**
	 * wait for the pin to go to value, then start timing until state change or timeout
	 * return the time for change in ms
	 * @param pinId
	 * @param value
	 * @param timeout (in microsecond)
	 * @return
	 * @throws IOException
	 */
	public abstract int pulseIn(int pinId, IoLevel value, int timeout) throws IOException;
	
	/**
	 * Emmet une impultion de 10ms à level
	 * si level = HIGH => passe à LOW, puis HIGH pendant 10 microS, puis reviens à LOW
	 * @param pinId
	 * @param value
	 * @throws IOException
	 */
	public abstract void pulseOut(int pinId, IoLevel level) throws IOException;
	
	// Méthodes d'obtention des pin, qui permettront d'utiliser les methodes d'I/O en portée pakage
	
	/**
	 * Obtains an digital input pin on this card
	 * @param pinId the pin id
	 * @return
	 * @throws InvalidParameterException if the pin was already in output mode or the id is incorrect
	 * @throws IOException in case of network error (for example : setPinMode on an arduino)
	 * @see InputPin
	 */
	public InputPin getInputPin(int pinId) throws InvalidParameterException, IOException {
		return new InputPin(pinId, this);
	}
	
	/**
	 * Obtains an digital output pin on this card
	 * @param pinId the pin id
	 * @return
	 * @throws InvalidParameterException if the pin was already in input mode or the id is incorrect
	 * @throws IOException in case of network error (for example : setPinMode on an arduino)
	 * @see OutputPin
	 */
	public OutputPin getOutputPin(int pinId) throws InvalidParameterException, IOException {
		return new OutputPin(pinId, this);
	}
	
	/**
	 * Obtains an analogic input pin on this card
	 * @param pinId the pin id
	 * @return
	 * @throws InvalidParameterException if the pin was already in output mode or the id is incorrect
	 * @throws IOException in case of network error (for example : setPinMode on an arduino)
	 * @see AnalogInputPin
	 */
	public AnalogInputPin getAnalogInputPin(int pinId) throws InvalidParameterException, IOException {
		return new AnalogInputPin(pinId, this);
	}
	
	/**
	 * Obtains an analogic output pin on this card
	 * @param pinId the pin id
	 * @return
	 * @throws InvalidParameterException if the pin was already in input mode or the id is incorrect
	 * @throws IOException in case of network error (for example : setPinMode on an arduino)
	 * @see AnalogOutputPin
	 */
	public AnalogOutputPin getAnalogOutputPin(int pinId) throws InvalidParameterException, IOException {
		return new AnalogOutputPin(pinId, this);
	}
}
