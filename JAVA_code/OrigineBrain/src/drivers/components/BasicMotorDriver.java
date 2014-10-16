package drivers.components;

import java.io.IOException;

import drivers.base.AnalogOutputPin;
import drivers.base.IoLevel;
import drivers.base.OutputPin;

/**
 * Classe de gestion d'un moteur 2 pins : sens et vitesse
 * @author robot
 *
 */
public class BasicMotorDriver {
	private OutputPin pinSens;
	private AnalogOutputPin pinVitesse;
	private int actualSens;
	private int actualVitesse;
	
	public BasicMotorDriver(OutputPin pinSens, AnalogOutputPin pinVitesse) {
		super();
		this.pinSens = pinSens;
		this.pinVitesse = pinVitesse;
		actualSens = 0;
	}
	
	/**
	 * Change le sens de rotation<br/>
	 * x<=0 => LOW sur la pin sens<br />
	 * x>0 => HIGH sur la pin sens
	 * @param x nouveau sens
	 */
	public void setSens(int x) throws IOException {
		actualSens = x;
		pinSens.digitalWrite(x <= 0 ? IoLevel.LOW : IoLevel.HIGH);
	}
	
	public void invertSens() throws IOException {
		if(actualSens==0) {
			setSens(1);
		} else {
			setSens(this.actualSens);
		}
	}
	
	/**
	 * Change la vitesse de rotation du moteur (1~255)
	 * @param v
	 * @throws IOException 
	 */
	public void setVitesse(int v) throws IOException {
		this.actualVitesse = v;
		pinVitesse.analogWrite(v);
	}

	public int getActualSens() {
		return actualSens;
	}

	public int getActualVitesse() {
		return actualVitesse;
	}
}
