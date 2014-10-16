package drivers.components;

import java.io.IOException;

import drivers.base.IoLevel;
import drivers.base.OutputPin;
import drivers.base.AnalogOutputPin;

/**
 * Represents an engine
 * Commande un moteur en fixant sa vitesse de rotation
 * @author jonathan
 *
 */

public class Engine {
	
	private OutputPin pinSens;
	private AnalogOutputPin pinSpeed;
	
	public Engine(OutputPin pinSens, OutputPin pinRight,AnalogOutputPin pinSpeed) throws IOException {
		this.pinSens = pinSens;
		this.pinSpeed = pinSpeed;
		
	}
	
	public void setSpeed(int speed) throws IOException {
		if(speed<0 && speed>(-255)) {
			pinSens.digitalWrite(IoLevel.LOW);
			pinSpeed.analogWrite(-speed);
		} 
		else if(speed>0 && speed<255) {	
			pinSens.digitalWrite(IoLevel.HIGH);
			pinSpeed.analogWrite(speed);
			}
		else {
			throw new IOException("Vitesse interdite");
		}
	}
	

}