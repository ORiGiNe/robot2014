package drivers.cardDrivers;

import java.io.IOException;

import drivers.base.IoMode;

/**
 * Represents a pin for the card driver<br />
 * it's state, it's value, it's mode, which operation can be done with it<br />
 * 
 * Could be a private inner class of BaseAbstractArduinoCardDriver, but it's also usefull
 * for the simuator, so ...
 * @author Jeremy
 *
 */
public class PinState {
	
	private int id;
	private IoMode mode;
	private boolean isAnalogicReadable;
	private boolean isAnalogicWritable;
	
	public PinState(int id, IoMode initialMode, boolean isAnalogicReadable, boolean isAnalogicWritable) {
		this.id = id;
		this.mode = initialMode;
		this.isAnalogicReadable = isAnalogicReadable;
		this.isAnalogicWritable = isAnalogicWritable;
	}

	public IoMode getMode() {
		return mode;
	}

	public void setMode(IoMode mode) throws IOException {
		if(getMode() != null && getMode() != mode) {
			throw new IOException("pin "+this.getId()+" was already in mode "+this.getMode());
		}
		this.mode = mode;
	}

	public int getId() {
		return id;
	}

	public boolean isAnalogicReadable() {
		return isAnalogicReadable;
	}

	public boolean isAnalogicWritable() {
		return isAnalogicWritable;
	}
}
