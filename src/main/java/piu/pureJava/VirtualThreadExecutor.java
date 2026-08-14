package piu.pureJava;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VirtualThreadExecutor {

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

		// 일반 플랫폼 스레드
		// try (ExecutorService executorService = Executors.newFixedThreadPool(10)) {
		// 	for (int i = 0; i < 10; i++) {
		// 		executorService.submit(runnable);    // submit task
		// 	}
		// 	executorService.close();    // AutoClose extend되어 있어서 불필요, try-with-resource
		// }

		// 권장 Executor 코드
		// Thread는 perTaskExecutor 사용하여 만들어서 쓰고 버리도록
		ThreadFactory factory = Thread.ofVirtual().name("myVirtual-", 0).factory();	// Virtual Thread 이름 설정
		try (ExecutorService executorService = Executors.newThreadPerTaskExecutor(factory)) {	// 일반 VirtualPerTask()는 설정 불가
			for (int i = 0; i < 100; i++) {
				executorService.submit(runnable);
			}
		}

		// close 끝난 후에 마지막에 호출
		log.info("main: 끝");
	}
}
