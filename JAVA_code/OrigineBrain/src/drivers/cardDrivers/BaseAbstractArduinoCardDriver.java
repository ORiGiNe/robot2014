package drivers.cardDrivers;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import network.base.BaseConnectionStream;
import network.interfaces.IConnection;
import drivers.base.AnalogInputPin;
import drivers.base.AnalogOutputPin;
import drivers.base.InputPin;
import drivers.base.IoMode;
import drivers.base.OutputPin;
import drivers.dtos.CommandType;

/**
 * Classe de base pour les driver de cartes arduino<br />
 * introduit le pin mode et sa vérification
 * @author Jeremy
 * @see BaseDistantCardDriver
 *
 */
public abstract class BaseAbstractArduinoCardDriver extends BaseDistantCardDriver {
	
	private Map<Integer, PinState> pinMap;

	public BaseAbstractArduinoCardDriver(IConnection connection) throws IOException {
		super(new BaseConnectionStream(connection));
		this.pinMap = new HashMap<Integer, PinState>(20);
		this.loadPins();
	}
	
	/**
	 * Ajoute une pin à la liste des pin disponibles sur la carte
	 * @param id
	 * @param isAnalogicReadable
	 * @param isAnalogicWritable
	 */
	protected void addPin(int id, boolean isAnalogicReadable, boolean isAnalogicWritable) {
		pinMap.put(id, new PinState(id, null, isAnalogicReadable, isAnalogicWritable));
	}
	
	/**
	 * Utilise addPin pour définir les pins présentes sur la carte
	 * @see BaseAbstractArduinoCardDriver#addPin(int, boolean, boolean, boolean, boolean)
	 */
	protected abstract void loadPins();
	
	/**
	 * Define the Io mode for a pin
	 * if already set : throw an InvalidParameterException
	 * @param pinId the pin identifier to set
	 * @param mode the mode to set
	 * @throws InvalidParameterException
	 * @throws IOException
	 */
	protected void setPinMode(int pinId, IoMode mode)  throws InvalidParameterException, IOException  {
		PinState pin = pinMap.get(pinId);
		if(pin==null) {
			throw new InvalidParameterException(this.getClass().getSimpleName()+" : can't set pin mode ("+mode.toString()
					+"), to pin number "+pinId+" : this pin doesn't exists");
		}
		
		try {
			pin.setMode(mode);
		} catch (IOException e) {
			throw new InvalidParameterException(this.getClass().getSimpleName()+" : "+e.getMessage());
		}
		
		this.sendMessage(CommandType.PIN_MODE, pinId, mode.value);
	}

	@Override
	public InputPin getInputPin(int pinId) throws InvalidParameterException, IOException  {
		PinState pin = pinMap.get(pinId);
		if(pin==null) {
			throw new InvalidParameterException("pin number "+pinId+" does not exists");
		}
		this.setPinMode(pinId, IoMode.INPUT);
		if(pin.isAnalogicReadable()) {
			return super.getAnalogInputPin(pinId);
		}
		return super.getInputPin(pinId);
	}

	@Override
	public OutputPin getOutputPin(int pinId) throws InvalidParameterException, IOException  {
		PinState pin = pinMap.get(pinId);
		if(pin==null) {
			throw new InvalidParameterException("pin number "+pinId+" does not exists");
		}
		this.setPinMode(pinId, IoMode.OUTPUT);
		if(pin.isAnalogicWritable()) {
			return super.getAnalogOutputPin(pinId);
		}
		return super.getOutputPin(pinId);
	}

	@Override
	public AnalogInputPin getAnalogInputPin(int pinId) throws InvalidParameterException, IOException  {
		PinState pin = pinMap.get(pinId);
		if(pin==null) {
			throw new InvalidParameterException("pin number "+pinId+" does not exists");
		}
		if(!pin.isAnalogicReadable()) {
			throw new InvalidParameterException("pin number "+pinId+" is not analogic readable");
		}
		this.setPinMode(pinId, IoMode.INPUT);
		return super.getAnalogInputPin(pinId);
	}

	@Override
	public AnalogOutputPin getAnalogOutputPin(int pinId) throws InvalidParameterException, IOException  {
		PinState pin = pinMap.get(pinId);
		if(pin==null) {
			throw new InvalidParameterException("pin number "+pinId+" does not exists");
		}
		if(!pin.isAnalogicWritable()) {
			throw new InvalidParameterException("pin number "+pinId+" is not analogic writable");
		}
		this.setPinMode(pinId, IoMode.OUTPUT);
		return super.getAnalogOutputPin(pinId);
	}
}
