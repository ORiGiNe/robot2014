package simulator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import network.interfaces.IConnectionStream;
import drivers.base.IoMode;
import drivers.cardDrivers.PinState;
import drivers.dtos.CommandDto;
import drivers.dtos.CommandType;


/**
 * Implémentation d'un "serveur" arduino
 * @author Jeremy
 * @see BaseArduinoCardDriver
 *
 */
public class ArduinoServeur extends Thread {
	
	/**
	 * Créé et lance un thread simulant une arduino<br />
	 * Stop it with stopListening
	 * @param stream
	 * @return the thread
	 * @see #stopListening()
	 */
	public static ArduinoServeur listenOn(IConnectionStream stream) {
		ArduinoServeur t = new ArduinoServeur(stream);
		t.start();
		return t;
	}
	
	protected Map<Integer, FullPinState> pinMap;
	
	public static final int A0 = 100;
	public static final int A1 = 101;
	public static final int A2 = 102;
	public static final int A3 = 103;
	public static final int A4 = 104;
	public static final int A5 = 105;
	
	private IConnectionStream stream;
	private boolean cont;
	
	public ArduinoServeur(IConnectionStream stream) {
		this.stream = stream;
		this.pinMap = new HashMap<Integer, FullPinState>();
		cont = true;
	}
	
	@Override
	public void run() {
		this.loadPins();
		
		cont = true;
		print("listening on "+stream);
		while(cont) {
			try {
				CommandDto dto = (CommandDto) stream.receiveObject(CommandDto.class);
				this.handleDto(dto);
			} catch (IOException e) {
				if(cont!=false) { //exception non prévue par stopListening
					this.handleException(e);
				}
				cont = false;
			}
		}
		this.close();
	}
	
	protected void handleException(Exception e) {
		e.printStackTrace();
	}
	
	protected void handleDto(CommandDto dto) throws IOException {
		//print("received : "+dto);
		FullPinState pin = pinMap.get(dto.pinId & 0xFF);
		if(pin == null) {
			throw new IOException("pin number "+dto.pinId+" does not exists");
		}
		CommandType command = CommandType.fromId(dto.commandId);
		if(command==null) {
			throw new IOException("Command of id "+dto.commandId+" does not exists");
		}
		switch(command) {
			case ANALOG_READ:
				this.stream.send((byte) pin.analogRead());
				break;
			case ANALOG_WRITE:
				pin.analogWrite(dto.parameter & 0xFF);
				break;
			case DIGITAL_READ:
				this.stream.send((byte) pin.digitalRead());
				break;
			case DIGITAL_WRITE:
				pin.digitalWrite(dto.parameter & 0xFF);
				break;
			case PIN_MODE:
				IoMode mode = IoMode.fromValue(dto.parameter);
				if(mode==null) {
					throw new IOException("IoMode of value "+dto.parameter+" does not exists");
				}
				pin.setMode(mode);
				break;
			default:
				break;
		}
	}
	
	/**
	 * Stop the thread. Use join to wait for it's terminaison
	 * @see Thread#join()
	 */
	public void stopListening() {
		this.cont = false;
		this.interrupt();
	}
	
	protected void close() {
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void addPin(int id, boolean isAnalogicReadable, boolean isAnalogicWritable) {
		pinMap.put(id, new FullPinState(id, null, isAnalogicReadable, isAnalogicWritable));
	}
	
	protected void loadPins() {
		this.addPin(0, false, false);
		this.addPin(1, false, false);
		this.addPin(2, false, false);
		this.addPin(3, false, true);
		this.addPin(4, false, false);
		this.addPin(5, false, true);
		this.addPin(6, false, true);
		this.addPin(7, false, false);
		this.addPin(8, false, false);
		this.addPin(9, false, true);
		this.addPin(10, false, true);
		this.addPin(11, false, true);
		this.addPin(12, false, false);
		this.addPin(13, false, false);
		
		this.addPin(A0, true, true);
		this.addPin(A1, true, true);
		this.addPin(A2, true, true);
		this.addPin(A3, true, true);
		this.addPin(A4, true, true);
		this.addPin(A5, true, true);
	}
	
	protected void print(String s) {
		System.out.println("\t\t\tFakeArduino : "+s);
	}
	
	/**
	 * Représente l'état d'une pin pour le serveur :<br />
	 * Opérations possibles, mode I/O et valeur courante
	 * @author Jeremy
	 *
	 */
	protected class FullPinState extends PinState {
		
		private int value;

		public FullPinState(int id, IoMode initialMode,
				boolean isAnalogicReadable, boolean isAnalogicWritable) {
			super(id, initialMode, isAnalogicReadable, isAnalogicWritable);
			this.value = 0;
		}

		public int getValue() {
			return value;
		}
		
		/**
		 * Change the value of the pin<br />
		 * Used by the simulator on pin in Input mode for changing the value
		 * read by the driver.<br />
		 * Assure that the value is correct regarding the pin type (analogic / digital)<br />
		 * @param value
		 */
		public void changeValue(int value) {
			if(!isAnalogicReadable() & value!=0) {
				value = 255;
			}
			if(value > 255) {
				value = 255;
			}
			if(value < 0) {
				value = 0;
			}
			this.value = value;
		}
		
		public int analogRead() throws IOException {
			if(getMode() != IoMode.INPUT) {
				throw new IOException("Can't read value to pin "+this.getId()+" : current mode is "+getMode());
			}
			if(!isAnalogicReadable()) {
				throw new IOException("pin number "+this.getId()+" is not analogic readable");
			}
			return this.value;
		}
		
		public int digitalRead() throws IOException {
			if(getMode() != IoMode.INPUT) {
				throw new IOException("Can't read value to pin "+this.getId()+" : current mode is "+getMode());
			}
			return this.value;
		}
		
		public void analogWrite(int x) throws IOException {
			if(getMode() != IoMode.OUTPUT) {
				throw new IOException("Can't write value to pin "+this.getId()+" : current mode is "+getMode());
			}
			if(!isAnalogicWritable()) {
				throw new IOException("pin number "+this.getId()+" is not analogic writable");
			}
			if(x<0 || x>255) {
				throw new IOException(x+" is not a correct value for analogic write");
			}
			this.value = x;
		}
		
		public void digitalWrite(int x) throws IOException {
			if(getMode() != IoMode.OUTPUT) {
				throw new IOException("Can't write value to pin "+this.getId()+" : current mode is "+getMode());
			}
			if(x!=0 & x!=255) {
				throw new IOException(x+" is not a correct value for digital write");
			}
			this.value = x;
		}
		
	}
}
