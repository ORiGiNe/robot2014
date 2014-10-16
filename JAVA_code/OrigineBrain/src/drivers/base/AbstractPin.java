package drivers.base;

/**
 * Represents a pin
 * @see InputPin
 * @see OutputPin
 * @see AnalogInputPin
 * @see AnalogOutputPin
 * @author Jeremy
 *
 */
public abstract class AbstractPin {

	protected int pinId;
	protected AbstractCardDriver cardDriver;
	
	AbstractPin(int pinId, AbstractCardDriver cardDriver) {
		this.pinId = pinId;
		this.cardDriver = cardDriver;
	}
	
	/**
	 * Get the card driver used by this pin
	 * @return
	 * @see AbstractCardDriver
	 */
	public AbstractCardDriver getDriver() {
		return cardDriver;
	}

	@Override
	public String toString() {
		return this.getClass().getName()+"[pinId=" + pinId + ", cardDriver=" + cardDriver.toString() + "]";
	}
}
