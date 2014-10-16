package drivers.components;

import java.io.IOException;
import drivers.base.AnalogOutputPin;

/**
 * Represents a servomotor
 * Commande un servomoteur en fixant son angle
 * @author jonathan
 *
 */
public class ServoMotor {
	
	private AnalogOutputPin pinAngle;
	
	public ServoMotor(AnalogOutputPin pinAngle) throws IOException {
		this.pinAngle = pinAngle;
	}
	
	public void setAngle(int angle) throws IOException {
		if(angle>=0 && angle<=255) {
			pinAngle.analogWrite(angle);
		} 
		else {
			throw new IOException("Angle interdit");
		}
	}
	

}