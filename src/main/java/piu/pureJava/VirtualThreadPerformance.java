package piu.pureJava;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VirtualThreadPerformance {

	private static long tmp;

	private static final Runnable runnable = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	};


	private static final Runnable cpuBoundRunnable = new Runnable() {
		@Override
		public void run() {
			long sum = 0;
			for (int i = 0; i < 10000000; i++) {
				sum += i;
			}
			tmp = sum;
		}
	};

	private static void platformThreadWithIOBound() {
		try (ExecutorService executorService = Executors.newFixedThreadPool(10000)) {
			for (int i = 0; i < 10000; i++) {
				executorService.submit(runnable);
			}
		}
	}

	private static void virtualThreadWithIOBound() {
		ThreadFactory factory = Thread.ofVirtual().name("myVirtual-", 0).factory();
		try (ExecutorService executorService = Executors.newThreadPerTaskExecutor(factory)) {
			for (int i = 0; i < 10000; i++) {
				executorService.submit(runnable);
			}
		}
	}

	private static void platformThreadWithCPUBound() {
		int cpuCount = Runtime.getRuntime().availableProcessors();
		try (ExecutorService executorService = Executors.newFixedThreadPool(cpuCount)) {
			for (int i = 0; i < 10000; i++) {
				executorService.submit(cpuBoundRunnable);
			}
		}
	}

	private static void virtualThreadWithCPUBound() {
		ThreadFactory factory = Thread.ofVirtual().name("myVirtual-", 0).factory();
		try (ExecutorService executorService = Executors.newThreadPerTaskExecutor(factory)) {
			for (int i = 0; i < 10000; i++) {
				executorService.submit(cpuBoundRunnable);
			}
		}
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		platformThreadWithIOBound();
		long endTime = System.currentTimeMillis();

		System.out.println("platform IO Bound 소요 시간: " + (endTime - startTime));	// 2.3s

		startTime = System.currentTimeMillis();
		virtualThreadWithIOBound();
		endTime = System.currentTimeMillis();

		System.out.println("virtual IO Bound 소요 시간: " + (endTime - startTime));	// 1.1s

		startTime = System.currentTimeMillis();
		platformThreadWithCPUBound();
		endTime = System.currentTimeMillis();

		System.out.println("platform CPU Bound 소요 시간: " + (endTime - startTime));	// 3.0s

		startTime = System.currentTimeMillis();
		virtualThreadWithCPUBound();
		endTime = System.currentTimeMillis();

		System.out.println("virtual CPU Bound 소요 시간: " + (endTime - startTime));	// 3.2s
	}
}
