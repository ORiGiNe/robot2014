package main;

import network.serial.ArduinoFinder;
import network.serial.SerialConnection;
import drivers.cardDrivers.ArduinoUnoCardDriver;

public class TestBidouille {
	public static void main(String[] args) {
		try {
			SerialConnection conn = ArduinoFinder.getArduinoByName("arduino_uno_1");
			ArduinoUnoCardDriver driver = new ArduinoUnoCardDriver(conn);
			
			//driver.attachInterrupt(0, AttachInterruptOption.RISING);
			
			
			System.out.println("Begin");
			while(true) {
				System.out.println(driver.getAttachedCount(0));
			}
			
			/*driver.closeConnection();
			ArduinoFinder.closeAllConnection();*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
