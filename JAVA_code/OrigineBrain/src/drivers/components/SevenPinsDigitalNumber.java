package drivers.components;

import java.io.IOException;

import drivers.base.IoLevel;
import drivers.base.OutputPin;

public class SevenPinsDigitalNumber {
	
	private OutputPin pinUp;
	private OutputPin pinUpRight;
	private OutputPin pinUpLeft;
	private OutputPin pinMidle;
	private OutputPin pinDownRight;
	private OutputPin pinDownLeft;
	private OutputPin pinDown;
	
	public SevenPinsDigitalNumber(OutputPin pinUp, OutputPin pinUpRight,
			OutputPin pinUpLeft, OutputPin pinMidle, OutputPin pinDownRight,
			OutputPin pinDownLeft, OutputPin pinDown) throws IOException {
		this.pinUp = pinUp;
		this.pinUpRight = pinUpRight;
		this.pinUpLeft = pinUpLeft;
		this.pinMidle = pinMidle;
		this.pinDownRight = pinDownRight;
		this.pinDownLeft = pinDownLeft;
		this.pinDown = pinDown;
		
		this.setValue(-1);
	}
	
	public void setValue(int value) throws IOException {
		if(value<0) {
			pinUp.digitalWrite(IoLevel.HIGH);
			pinUpRight.digitalWrite(IoLevel.HIGH);
			pinUpLeft.digitalWrite(IoLevel.HIGH);
			pinMidle.digitalWrite(IoLevel.HIGH);
			pinDownRight.digitalWrite(IoLevel.HIGH);
			pinDownLeft.digitalWrite(IoLevel.HIGH);
			pinDown.digitalWrite(IoLevel.HIGH);
		} else {
			pinUp.digitalWrite(value ==0 || value == 2 || value==3 || value >= 5 ? IoLevel.LOW : IoLevel.HIGH);
			pinUpRight.digitalWrite(value ==8 || value <= 4 || value >= 7 ? IoLevel.LOW : IoLevel.HIGH);
			pinUpLeft.digitalWrite(value == 0 || value ==4 || value ==5 || value ==6 || value >= 8? IoLevel.LOW : IoLevel.HIGH);
			pinMidle.digitalWrite((value >= 2 && value <=6) || value >= 8 ? IoLevel.LOW : IoLevel.HIGH);
			pinDownRight.digitalWrite(value <=1 || value >=3 ? IoLevel.LOW : IoLevel.HIGH);
			pinDownLeft.digitalWrite(value ==0 || value == 2 || value ==6 || value == 8 ? IoLevel.LOW : IoLevel.HIGH);
			pinDown.digitalWrite(value == 0 || value == 2 || value ==3 || value ==5 || value == 6 || value >= 8? IoLevel.LOW : IoLevel.HIGH);
		}
	}

	public void setValueNotKnow() throws IOException {
		pinUp.digitalWrite(IoLevel.HIGH);
		pinUpRight.digitalWrite(IoLevel.HIGH);
		pinUpLeft.digitalWrite(IoLevel.HIGH);
		pinMidle.digitalWrite(IoLevel.LOW);
		pinDownRight.digitalWrite(IoLevel.HIGH);
		pinDownLeft.digitalWrite(IoLevel.HIGH);
		pinDown.digitalWrite(IoLevel.HIGH);
	}
	

}
