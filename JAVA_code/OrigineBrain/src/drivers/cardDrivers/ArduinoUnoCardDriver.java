package drivers.cardDrivers;

import java.io.IOException;

import network.interfaces.IConnection;

/**
 * Driver pour carte arduino uno<br />
 * Les pin Ax ont l'id 10x<br />
 * Des constantes sont disponibles : A0, A1, etc ...
 * @see ArduinoUnoCardDriver#loadPins()
 * @author Jeremy
 *
 */
public class ArduinoUnoCardDriver extends BaseAbstractArduinoCardDriver {
	
	public static final int A0 = 14;
	public static final int A1 = 15;
	public static final int A2 = 16;
	public static final int A3 = 17;
	public static final int A4 = 18;
	public static final int A5 = 19;

	public ArduinoUnoCardDriver(IConnection connection) throws IOException {
		super(connection);
	}

	@Override
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
}
