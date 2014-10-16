package drivers.base;

import java.io.IOException;

/**
 * Represents an digital output pin
 * Can only be obtained by BaseCardDriver.getOutputPin
 * @see AbstractCardDriver#getOutputPin(int)
 * @author Jeremy
 *
 */
public class OutputPin extends AbstractPin {
	
	int lastValue;
	
	OutputPin(int id, AbstractCardDriver cardDriver) {
		super(id, cardDriver);
		this.lastValue = 0;
	}

	/**
	 * Change the state of this pin
	 * @param level the new pin level
	 * @throws IOException in case of communication error with the card
	 * @see AbstractCardDriver#digitalWrite(int, IoLevel)
	 */
	public void digitalWrite(IoLevel level) throws IOException {
		this.cardDriver.digitalWrite(pinId, level);
		this.lastValue = level.value & 0xff;
	}
	
	/**
	 * Retrives the last value sucessfuly sended to the card as an int
	 * @return
	 * @see IoLevel#fromValue(int)
	 */
	public int getLastValueWrittent() {
		return lastValue;
	}
	
	/**
	 * Emet une impulsion à level
	 * @param level
	 * @throws IOException 
	 */
	public void pulseOut(IoLevel level) throws IOException {
		this.cardDriver.pulseOut(this.pinId, level);
	}
}
