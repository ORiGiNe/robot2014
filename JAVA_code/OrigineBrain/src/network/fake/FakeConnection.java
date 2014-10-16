package network.fake;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;

import network.interfaces.IConnection;

/**
 * 
 * @author Jeremy
 *
 */
public class FakeConnection {
	
	protected FakeConnectionSide side1;
	protected FakeConnectionSide side2;
	
	protected Queue<Byte> queue1;
	protected Queue<Byte> queue2;
	
	public FakeConnection() {
		queue1 = new LinkedList<Byte>();
		queue2 = new LinkedList<Byte>();
		side1 = new FakeConnectionSide(queue1, queue2);
		side2 = new FakeConnectionSide(queue2, queue1);
	}
	
	public IConnection getSide1() {
		return side1;
	}
	
	public IConnection getSide2() {
		return side2;
	}

	/**
	 * Représente un côté de connection
	 * @author Jeremy
	 *
	 */
	public class FakeConnectionSide implements IConnection {
		private Queue<Byte> dataqueueIn;
		private Queue<Byte> dataqueueOut;
		private FakeInputStream inputStream;
		private FakeOutputStream outputStream;
		private boolean closed;
		
		public FakeConnectionSide(Queue<Byte> queueIn, Queue<Byte> queueOut) {
			this.dataqueueIn = queueIn;
			this.dataqueueOut = queueOut;
			inputStream = new FakeInputStream(queueIn);
			outputStream = new FakeOutputStream(queueOut);
			closed = false;
		}
		
		@Override
		public InputStream getInputStream() {
			return inputStream;
		}

		@Override
		public OutputStream getOutputStream() {
			return outputStream;
		}

		@Override
		public synchronized void close() {
			if(!closed) {
				try {
					inputStream.close();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				closed = true;
			}
		}

		@Override
		public boolean isClosed() {
			return closed;
		}
		
		public Queue<Byte> getInputQueue() {
			return dataqueueIn;
		}
		
		public Queue<Byte> getOutputQueue() {
			return dataqueueOut;
		}
		
		@Override
		public String toString() {
			return this.getClass().getSimpleName();
		}
	}

	/**
	 * OutputStream qui écrit dans une queue
	 * @author Jeremy
	 *
	 */
	protected static class FakeOutputStream extends OutputStream {
		
		public Queue<Byte> queue;
		
		public FakeOutputStream(Queue<Byte> queue) {
			this.queue = queue;
		}
		
		@Override
		public void write(int x) throws IOException {
			synchronized (queue) {
				queue.add((byte) x);
				queue.notifyAll();
			}
		}

	}
	
	/**
	 * InputStream qui lit depuis une queue
	 * @author Jeremy
	 *
	 */
	protected static class FakeInputStream extends InputStream {
		
		public Queue<Byte> queue;
		
		public FakeInputStream(Queue<Byte> queue) {
			this.queue = queue;
		}
		
		@Override
		public int read() throws IOException {
			synchronized (queue) {
				Byte b = queue.poll();
				while(b==null) {
					try {
						queue.wait();
					} catch (InterruptedException e) {
						throw new IOException("Interrupted", e);
					}
					b = queue.poll();
				}
				return b & 0xFF; //cast en int "propre" => byte non singé !
			}
		}
	}
}
