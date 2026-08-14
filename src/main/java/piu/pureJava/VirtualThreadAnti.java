package piu.pureJava;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VirtualThreadAnti {

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

	public static void main(String[] args) {

		// Virtual Thread가 1개로 제한
		// 스레드 풀 사용 X, 비용이 적으니 그냥 만들고 버리는 것이 좋다. (perTask)
		ThreadFactory factory = Thread.ofVirtual().name("myVirtual-", 0).factory();
		try (ExecutorService executorService = Executors.newFixedThreadPool(1, factory)) {	// or newSingle도 안티패턴
			for (int i = 0; i < 100; i++) {
				executorService.submit(runnable);
			}
		}
	}

}
