package main;

import network.serial.ArduinoFinder;
import network.serial.SerialConnection;
import drivers.base.InputPin;
import drivers.base.OutputPin;
import drivers.cardDrivers.ArduinoUnoCardDriver;
import drivers.components.UltrasonDetector;

public class SerialTest {
	public static void main(String[] args) {
		try {
			SerialConnection conn3 = ArduinoFinder.getArduinoByName("arduino_uno_3");
			ArduinoUnoCardDriver driver = new ArduinoUnoCardDriver(conn3);
			
			OutputPin trigger = driver.getOutputPin(2);
			InputPin echo = driver.getInputPin(3);
			
			UltrasonDetector detector = new UltrasonDetector(trigger, echo);
			
			while(true) {
				System.out.println(detector.detect());
				Thread.sleep(500);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
