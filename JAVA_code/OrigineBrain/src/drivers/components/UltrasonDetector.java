package drivers.components;

import java.io.IOException;

import drivers.base.InputPin;
import drivers.base.IoLevel;
import drivers.base.OutputPin;

public class UltrasonDetector {
	private OutputPin trigger;
	private InputPin echo;
	
	public UltrasonDetector(OutputPin trigger, InputPin echo) {
		super();
		this.trigger = trigger;
		this.echo = echo;
	}
	
	public float detect() throws IOException {
		float time;
		do {
			trigger.pulseOut(IoLevel.HIGH);
			time = echo.pulseIn(IoLevel.HIGH, 5000);
		} while(time <= 0);
		return (float) (time/58.0);
	}
	
}
