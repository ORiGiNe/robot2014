package network.serial;

import java.io.IOException;

public class ArduinoSerialConnection extends SerialConnection {

	public final static int READY_CODE = 42;

	private String name;

	public ArduinoSerialConnection(String portName) throws IOException {
		super(portName);

		// lecture d'un octet, envoyé par la arduino une fois qu'elle est prête
		// (démarrée)
		int r;

		try {
			r = this.getInputStream().read();
		} catch (IOException e) {
			this.close();
			throw e;
		}

		if (r != READY_CODE) {
			this.close();
			throw new IOException("received " + r
					+ " instead of normal ready code (" + READY_CODE + ")");
		}

		try {
			this.name = this.readName();
		} catch (Exception e) {
			this.close();
			throw e;
		}
	}

	public synchronized String getName() {
		return this.name;
	}

	private synchronized String readName() throws IOException {
		int size = this.getInputStream().read();
		byte[] buffer = new byte[size];
		this.getInputStream().read(buffer);
		return new String(buffer);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"[name=" + name + ", port="
				+ getPortName() + "]";
	}

}
