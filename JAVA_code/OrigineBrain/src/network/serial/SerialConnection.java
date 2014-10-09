package network.serial;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import network.interfaces.IConnection;

public class SerialConnection implements IConnection, SerialPortEventListener {

	public static int TIME_OUT = 1000;
	public static String appName = "RobalBrain";
	public static int BIT_RATE = 9600;

	/**
	 * RXTX n'est pas thread safe => 2 open simultannés donne une segfault Cet
	 * object est synchronisé et empêche ce bug d'arriver
	 */
	private final static Object lock = new Object();

	public static List<String> getPortList() throws Exception {
		Enumeration<?> ports = CommPortIdentifier.getPortIdentifiers();
		List<String> list = new LinkedList<String>();

		while (ports.hasMoreElements()) {
			CommPortIdentifier curPort = (CommPortIdentifier) ports
					.nextElement();
			if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				list.add(curPort.getName());
			}
		}

		return list;
	}

	private boolean closed;
	private SerialPort serialPort;

	private Queue<Integer> readingQueue;

	private OutputStream out;
	private InputStream rawIn;
	private SerialInputStream serialIn;

	private String portName;

	public SerialConnection(String portName) throws IOException {
		try {
			this.portName = portName;

			CommPortIdentifier portIdentifier = CommPortIdentifier
					.getPortIdentifier(portName);

			if (portIdentifier.isCurrentlyOwned()) {
				throw new IOException("port " + portName
						+ " currently used by "
						+ portIdentifier.getCurrentOwner());
			}

			CommPort comPort = null;
			synchronized (lock) { // un seul thread peut faire open à la fois =>
									// RXTX n'est pas thread safe
				comPort = portIdentifier.open(appName, TIME_OUT);
			}

			if (!(comPort instanceof SerialPort)) {
				throw new IOException("port " + portName + " is not serial");
			}

			this.serialPort = (SerialPort) comPort;
			serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			this.out = serialPort.getOutputStream();
			this.rawIn = serialPort.getInputStream();

			this.readingQueue = new LinkedList<Integer>();
			this.serialIn = new SerialInputStream(this.readingQueue);

			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);

			this.closed = false;
		} catch (Exception e) {
			this.close();
			throw new IOException("unable to connect to port " + portName, e);
		}
	}

	@Override
	public void serialEvent(SerialPortEvent event) { 
		//Pas synchronized => RXTX bloque sinon (...)
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			int data;
			try {
				data = this.rawIn.read();
			} catch (IOException e) {
				data = -1;
			}
			synchronized (this.readingQueue) {
				this.readingQueue.add(data);
				this.readingQueue.notifyAll();
			}
		}
	}

	@Override
	public synchronized void close() throws IOException {
		if (!this.closed) {
			if (this.out != null)
				this.out.close();
			if (this.rawIn != null)
				this.rawIn.close();
			if (this.serialIn != null)
				this.serialIn.close();

			if (this.serialPort != null) {
				this.serialPort.removeEventListener();
				this.serialPort.close();
			}
			this.closed = true;
		}
	}

	public String getPortName() {
		return this.portName;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return this.serialIn;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return this.out;
	}

	@Override
	public synchronized boolean isClosed() {
		return false;
	}

	public static class SerialInputStream extends InputStream {

		private Queue<Integer> queue;

		private SerialInputStream(Queue<Integer> queue) {
			this.queue = queue;
		}

		@Override
		public synchronized int read() throws IOException {
			synchronized (this.queue) {
				Integer b = queue.poll();
				while (b == null) {
					try {
						queue.wait();
					} catch (InterruptedException e) {
						throw new IOException("reading interrupted", e);
					}
					b = queue.poll();
				}
				return b & 0xFF;
			}
		}
	}
}
