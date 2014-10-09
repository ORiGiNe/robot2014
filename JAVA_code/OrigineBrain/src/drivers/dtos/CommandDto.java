package drivers.dtos;

import network.base.BaseDto;

public class CommandDto extends BaseDto {
	
	public CommandDto(CommandType command, int pinId, byte parameter) {
		super();
		this.commandId = command.id;
		this.pinId = (byte) pinId;
		this.parameter = parameter;
	}
	
	public CommandDto() {
		this(CommandType.DIGITAL_WRITE, 0, (byte) 0);
	}

	@Override
	public String toString() {
		return "CommandDto [command=" + CommandType.fromId(commandId) + ", pinId=" + (pinId & 0xff)
				+ ", parameter=" + (parameter & 0xff) + "]";
	}

	public byte commandId;
	
	public byte pinId;
	
	public byte parameter; //valeur du write
}
