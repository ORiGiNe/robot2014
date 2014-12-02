package main;

import network.serial.ArduinoFinder;
import drivers.base.InputPin;
import drivers.base.OutputPin;
import drivers.cardDrivers.ArduinoUnoCardDriver;
import drivers.components.UltrasonDetector;

public class TestUltrasonDetector {
	
	public static void main(String[] args) {
		try {
			ArduinoUnoCardDriver driver = new ArduinoUnoCardDriver(ArduinoFinder.getArduinoByName("arduino_uno_1"));
			
			InputPin pin12 = driver.getInputPin(12);
			OutputPin pin13 = driver.getOutputPin(13);
			
			UltrasonDetector ud = new UltrasonDetector(pin13,pin12);
			
			System.out.print("Toto");
			System.out.print(ud.detect());
			
			int i;
			
			for (i=0;i<10;i=i+1){
				System.out.print(ud.detect());
				System.out.print("Test");
				Thread.sleep(500);
			}
			
			driver.closeConnection();
			ArduinoFinder.closeAllConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
