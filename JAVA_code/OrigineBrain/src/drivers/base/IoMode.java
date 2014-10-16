package drivers.base;

/**
 * Represents an I/O mode (INPUT / OUTPUT)
 * @author Jeremy
 *
 */
public enum IoMode { INPUT(0), OUTPUT(1);

	public byte value;
	
	IoMode(int v) { value = (byte) v; }
	
	/**
	 * Get a mode from it's value
	 * @param v
	 * @return
	 * @see IoMode
	 * @see IoLevel
	 */
	public static IoMode fromValue(int v) {
		return (v==0) ? IoMode.INPUT : IoMode.OUTPUT;
	}

}
