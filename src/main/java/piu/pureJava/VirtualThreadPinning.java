package piu.pureJava;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * Synchronized 혹은 Native Method 사용 시 Pinning 문제
 *
 * Synchronized(EntrySet -> Owner(1) -> waitSet(wait()))
 * 		모니터락 사용 -> ObjectMonitor C++ Structure -> 이 락 소유권 정보는 플랫폼 스레드의 스택에 바인딩 -> 이 상황에서 JVM 레벨의 Virtual이 언마운트되면 소유권 정보가 깨짐 -> 그래서 Pinning 시켜둠
 *
 * Native Method -> Object.hashCode 같이 C/C++의 Native 언어로 만들어진 Library 쪽에서 실행되는 메서드
 * 		Virtual Thread의 제어권이 JVM을 벗어나 native 영역으로 넘어갔기에, Virtual Thread 언마운트 X
 *
 */
@Slf4j
public class VirtualThreadPinning {

	public static void pinning() {

		// newVirtualThreadPerTaskExecutor() or VirtualFactory로 생성하면 기본 스케쥴러가 ForkJoinPool이고,
		// 코어 개수만큼 플랫폼 스레드 사용, Synchronized Pinning으로 인해 코어 개수만큼 버퍼링이 걸릴 것.
		// 정리: I/O Burst가 많아도 Synchronized로 인해 Pinning되면 효율이 좋지 못하다.
		ThreadFactory factory = Thread.ofVirtual().name("myVirtual-", 0).factory();
		try (ExecutorService executorService = Executors.newThreadPerTaskExecutor(factory)) {
			for (int i = 0; i < 100; i++) {
				Pinning pinning = new Pinning();
				executorService.submit(pinning.getRunnable());
			}
		}
	}

	public static void main(String[] args) {
		pinning();
	}
}
