package drivers.cardDrivers;

import java.io.IOException;

import network.interfaces.IConnectionStream;
import drivers.base.AbstractCardDriver;
import drivers.base.IoLevel;
import drivers.dtos.CommandDto;
import drivers.dtos.CommandType;

/**
 * Driver de cartes distantes, via un connectionStream
 * Implémentation du protocole avec les CommandDto
 * @author Jeremy
 * @see AbstractCardDriver
 *
 */
public class BaseDistantCardDriver extends AbstractCardDriver {

	protected IConnectionStream stream;
	private CommandDto comDto;
	
	public BaseDistantCardDriver(IConnectionStream stream) {
		super();
		this.stream = stream;
		this.comDto = new CommandDto();
	}
	
	/**
	 * Send a message to the Card
	 * @param type
	 * @param pinId
	 * @param parameter
	 * @throws IOException
	 */
	protected synchronized void sendMessage(CommandType type, int pinId, byte parameter) throws IOException {
		synchronized (comDto) {
			comDto.commandId = type.id;
			comDto.pinId = (byte) pinId;
			comDto.parameter = parameter;
			stream.send(comDto);
		}
	}
	
	/**
	 * retrieve an int value from the stream and return it
	 * @return
	 * @throws IOException
	 */
	protected synchronized int getReadValue() throws IOException {
		return ((byte) stream.receiveObject(Byte.class)) & 0xff;
	}

	@Override
	protected synchronized IoLevel digitalRead(int pinId) throws IOException {
		this.sendMessage(CommandType.DIGITAL_READ, pinId, (byte) 0);
		return IoLevel.fromValue(getReadValue());
	}
	
	@Override
	protected synchronized int analogRead(int pinId) throws IOException {
		this.sendMessage(CommandType.ANALOG_READ, pinId, (byte) 0);
		return getReadValue();
	}

	@Override
	protected synchronized void digitalWrite(int pinId, IoLevel level) throws IOException {
		this.sendMessage(CommandType.DIGITAL_WRITE, pinId, level.value);
	}

	@Override
	protected synchronized void analogWrite(int pinId, int level) throws IOException {
		this.sendMessage(CommandType.ANALOG_WRITE, pinId, (byte) level);
	}
	
	@Override
	public synchronized int pulseIn(int pinId, IoLevel value, int timeout) throws IOException {
		this.sendMessage(CommandType.PULSE_IN, pinId, value.value);
		this.stream.send(timeout);
		int rep = (int) this.stream.receiveObject(Integer.class);
		return rep;
	}
	
	@Override
	public void pulseOut(int pinId, IoLevel level) throws IOException {
		this.sendMessage(CommandType.PULSE_OUT, pinId, level.value);
	}

	public void closeConnection() {
		try {
			this.stream.close();
		} catch (IOException e) { }
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"[stream="+stream+"]";
	}
	
	

}
