package drivers.components;

import java.io.IOException;

import drivers.base.AnalogInputPin;
import drivers.base.IoLevel;


public class ServoWheel {
	
	private AnalogInputPin pinChA;
	private AnalogInputPin pinChB;
	private AnalogInputPin pinChI;
	
	public ServoWheel(AnalogInputPin pinChA, AnalogInputPin pinChB, AnalogInputPin pinChI) throws IOException {
		this.pinChA = pinChA;
		this.pinChB = pinChB;
		this.pinChI = pinChI;
		
	}
	
	public float vitesse() throws IOException {
		return 0;
		
	}
	
	public float distance() throws IOException {
		return 0;
	}
	

}