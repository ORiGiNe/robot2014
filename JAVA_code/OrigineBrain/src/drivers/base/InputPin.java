package drivers.base;

import java.io.IOException;

/**
 * Represents an digital input pin
 * Can only be obtained by BaseCardDriver.getInputPin
 * @see AbstractCardDriver#getInputPin(int)
 * @author Jeremy
 *
 */
public class InputPin extends AbstractPin {
	
	InputPin(int pinId, AbstractCardDriver cardDriver) {
		super(pinId, cardDriver);
	}

	/**
	 * Read a digital value from the card
	 * @return the read level
	 * @throws IOException in case of communication error with the card
	 * @see AbstractCardDriver#digitalRead(int)
	 */
	public IoLevel digitalRead() throws IOException {
		return cardDriver.digitalRead(pinId);
	}
	
	public int pulseIn(IoLevel level, int timeout) throws IOException {
		return cardDriver.pulseIn(pinId, level, timeout);
	}
	
}
