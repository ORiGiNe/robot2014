package drivers.base;

import drivers.dtos.CommandType;

public enum AttachInterruptOption {
	RISING(3), FALLING(2), CHANGE(1),
	LOW(0);
	
	public byte id;
	
	AttachInterruptOption(int val) {
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
