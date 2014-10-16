package main;

import network.serial.ArduinoFinder;
import network.serial.SerialConnection;
import drivers.base.IoLevel;
import drivers.base.OutputPin;
import drivers.base.AnalogOutputPin;
import drivers.cardDrivers.ArduinoUnoCardDriver;

public class MainClient {
	
	public static void main(String[] args) {
		try {
			SerialConnection conn1 = ArduinoFinder.getArduinoByName("arduino_uno_2");
			ArduinoUnoCardDriver driver1 = new ArduinoUnoCardDriver(conn1);
			
			
			AnalogOutputPin pin11 = driver1.getAnalogOutputPin(11);
			
			
			pin11.analogWrite(125);
			Thread.sleep(5000);
		
			
			
			driver1.closeConnection();
			ArduinoFinder.closeAllConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
