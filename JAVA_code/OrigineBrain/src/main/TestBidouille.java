package main;

import network.serial.ArduinoFinder;
import network.serial.SerialConnection;
import drivers.base.InputPin;
import drivers.base.OutputPin;
import drivers.cardDrivers.ArduinoUnoCardDriver;
import drivers.components.UltrasonDetector;

public class TestBidouille {
	public static void main(String[] args) {
		try {
			SerialConnection conn = ArduinoFinder.getArduinoByName("arduino_uno_1");
			ArduinoUnoCardDriver driver = new ArduinoUnoCardDriver(conn);
			
			//driver.attachInterrupt(0, AttachInterruptOption.RISING);
			//System.out.println(driver.getAttachedCount(0));
			
			OutputPin trigger = driver.getOutputPin(12);
			InputPin echo = driver.getInputPin(11);
			
			UltrasonDetector detector = new UltrasonDetector(trigger, echo);
			
			System.out.println("Begin");
			while(true) {
				System.out.println(detector.detect());
				Thread.sleep(500);
			}
			
			/*driver.closeConnection();
			ArduinoFinder.closeAllConnection();*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
