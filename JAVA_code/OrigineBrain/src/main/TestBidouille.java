package main;

import network.serial.ArduinoFinder;
import network.serial.SerialConnection;
import drivers.base.AnalogOutputPin;
import drivers.base.OutputPin;
import drivers.cardDrivers.ArduinoUnoCardDriver;
import drivers.components.BasicMotorDriver;

public class TestBidouille {
	public static void main(String[] args) {
		try {
			SerialConnection conn = ArduinoFinder.getArduinoByName("arduino_uno_3");
			ArduinoUnoCardDriver driver = new ArduinoUnoCardDriver(conn);
			
			OutputPin pin4 = driver.getOutputPin(4);
			AnalogOutputPin pin5 = driver.getAnalogOutputPin(5);
			
			BasicMotorDriver motorR = new BasicMotorDriver(pin4, pin5);
			motorR.setSens(1);
			motorR.setVitesse(100);
			
			Thread.sleep(3000);
			
			motorR.setVitesse(0);
			
			Thread.sleep(3000);
			
			motorR.setSens(-1);
			motorR.setVitesse(100);
			
			Thread.sleep(3000);
			
			motorR.setVitesse(0);
			
			driver.closeConnection();
			ArduinoFinder.closeAllConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
