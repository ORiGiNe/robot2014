package drivers.base;

import java.io.IOException;

/**
 * Represents an analogic output pin
 * can only be obtained by BaseCardDriver.getAnalogOutputPin
 * @see AbstractCardDriver#getAnalogOutputPin(int)
 * @author Jeremy
 *
 */
public class AnalogOutputPin extends OutputPin {
	
	AnalogOutputPin(int id, AbstractCardDriver cardDriver) {
		super(id, cardDriver);
	}

	/**
	 * Change the pin value, from 0 to 255 included
	 * @param level the new level ( 0 <= level <= 255)
	 * @throws IOException in case of communication error or bad level value
	 * @see AbstractCardDriver#analogWrite(int, int)
	 */
	public void analogWrite(int level) throws IOException {
		if(level<0 || level > 255) {
			throw new IOException("level value must be between 0 and 255");
		}
		cardDriver.analogWrite(pinId, level);
		lastValue = level;
	}
	
}
