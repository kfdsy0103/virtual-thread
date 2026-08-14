package piu.pureJava;

import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Pinning {

	private final ReentrantLock lock = new ReentrantLock();

	// 서드파티 라이브러리 코드 뜯는게 어려운 경우 Detect 어떻게? (Add JVM options)
	// 		-Djdk.tracePinnedThreads=full or -Djdk.tracePinnedThreads=short
	private final Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// Pinning
			synchronized (this) {
				log.info("Sleep 전 thread: {}, class: {}", Thread.currentThread(), Thread.currentThread().getClass());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				log.info("Sleep 후 thread: {}, class: {}", Thread.currentThread(), Thread.currentThread().getClass());
			}
		}
	};

	private final Runnable reentrantLockRunnable = new Runnable() {
		@Override
		public void run() {

			log.info("Sleep 전 thread: {}, class: {}", Thread.currentThread(), Thread.currentThread().getClass());

			lock.lock();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} finally {
				lock.unlock();
			}

			log.info("Sleep 후 thread: {}, class: {}", Thread.currentThread(), Thread.currentThread().getClass());
		}
	};

	public Runnable getRunnable() {
		return this.runnable;
	}

	public Runnable getReentrantLockRunnable() {
		return this.reentrantLockRunnable;
	}
}
