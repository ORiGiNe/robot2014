package drivers.base;

/**
 * Represents a digital level with LOW and HIGH values
 * @author Jeremy
 *
 */
public enum IoLevel { LOW(0), HIGH(1);

	public byte value;
	
	IoLevel(int v) { value = (byte) v; }
	
	/**
	 * Get a level from it's value
	 * @param v
	 * @return
	 * @see IoLevel
	 * @see IoMode
	 */
	public static IoLevel fromValue(int v) {
		return (v==0) ? IoLevel.LOW : IoLevel.HIGH;
	}
	
}