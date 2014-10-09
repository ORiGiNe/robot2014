package tools;

public class DelayExecution {

	protected Runnable runner;
	protected int delay;
	protected RunnerThread t;
	
	public DelayExecution(Runnable runner) {
		this.runner = runner;
		this.delay = 0;
		this.t = null;
	}
	
	public void start(int delay) {
		if(t!=null && t.isAlive()) {
			t.restart(delay);
		} else {
			t = new RunnerThread(delay, this.runner);
			t.start();
		}
	}
	
	public void stop() {
		if(t!=null && t.isAlive()) {
			t.exit();
		}
	}
	
	protected class RunnerThread extends Thread {

		protected Integer delay;
		protected boolean cont;
		protected Runnable runner;
		protected Object lock;
		

		public RunnerThread(Integer delay, Runnable runner) {
			super();
			this.delay = delay;
			this.runner = runner;
			this.lock = new Object();
			this.cont = true;
		}

		@Override
		public void run() {
			this.cont = true;
			while(cont) {
				synchronized (this.delay) {
					try {
						Thread.sleep(delay);
						this.runner.run();
						this.cont = false;
					} catch (InterruptedException e) { }
				}
				synchronized (lock) { }
			}
		}
		
		public void exit() {
			this.cont = false;
			this.interrupt();
		}
		
		public void restart(int delay) {
			synchronized (lock) {
				this.interrupt();
				synchronized (this.delay) {
					this.delay = delay;
				}
			}
		}
	}

}
