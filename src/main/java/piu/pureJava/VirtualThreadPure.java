package piu.pureJava;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VirtualThreadPure {

	private static final Runnable runnable = new Runnable() {
		@Override
		public void run() {
			log.info("Sleep 전 thread: {}, class: {}", Thread.currentThread(), Thread.currentThread().getClass());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			log.info("Sleep 후 thread: {}, class: {}", Thread.currentThread(), Thread.currentThread().getClass());
		}
	};

	// main은 non daemon, non-daemon x -> JVM down
	public static void main(String[] args) throws InterruptedException {

		// Thread thread = new Thread(runnable);
		// thread.start();	// non daemon

		Thread thread = Thread.ofVirtual().name("myVirtual").unstarted(runnable);
		thread.start();
		thread.join();	// ForkJoinPool(Daemon) -> non daemon인 main에서 join
	}
}
