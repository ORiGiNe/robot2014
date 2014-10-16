package main;

import network.serial.ArduinoFinder;
import drivers.base.AnalogOutputPin;
import drivers.base.IoLevel;
import drivers.base.OutputPin;
import drivers.cardDrivers.ArduinoUnoCardDriver;
import drivers.components.ServoMotor;

public class MainClient {
	
	public static void main(String[] args) {
		try {
			ArduinoUnoCardDriver driver = new ArduinoUnoCardDriver(ArduinoFinder.getArduinoByName("arduino_uno_1"));
			
			AnalogOutputPin pin11 = driver.getAnalogOutputPin(11);
			OutputPin pin13 = driver.getOutputPin(13);
			
			ServoMotor motor = new ServoMotor(pin11);
			
			int angle;
			
			for (angle=250;angle>=0;angle=angle-10){
				pin13.digitalWrite(IoLevel.HIGH);
				motor.setAngle(angle);	
				Thread.sleep(500);
				pin13.digitalWrite(IoLevel.LOW);
				Thread.sleep(500);
			}
			
			driver.closeConnection();
			ArduinoFinder.closeAllConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
