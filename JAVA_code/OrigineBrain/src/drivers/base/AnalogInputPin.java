package drivers.base;

import java.io.IOException;

/**
 * Represents an anlogic input pin
 * can only be obtained by BaseCardDriver.getAnalogInputPin
 * @see AbstractCardDriver#getAnalogInputPin(int)
 * @author Jeremy
 *
 */
public class AnalogInputPin extends InputPin {
	
	AnalogInputPin(int pinId, AbstractCardDriver cardDriver) {
		super(pinId, cardDriver);
	}

	/**
	 * Read an analogic value from the card between 0 and 255
	 * @return the read value
	 * @throws IOException in case of communication error with the card
	 * @see AbstractCardDriver#analogRead(int)
	 */
	public int analogRead() throws IOException {
		return cardDriver.analogRead(pinId);
	}
	
}
