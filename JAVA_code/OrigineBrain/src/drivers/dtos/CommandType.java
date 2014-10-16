package drivers.dtos;

public enum CommandType {
	PIN_MODE(1), DIGITAL_READ(2), DIGITAL_WRITE(3),
	ANALOG_READ(4), ANALOG_WRITE(5), PULSE_IN(6), PULSE_OUT(7);
	
	public byte id;
	
	CommandType(int val) {
		this.id = (byte) val;
	}
	
	/**
	 * Retrives a commandType from it's id<br />
	 * @param id
	 * @return the commandType if exists, else null
	 */
	public static CommandType fromId(int id) {
		for(CommandType type : CommandType.values()) {
			if(type.id==id)
				return type;
		}
		return null;
	}
}
