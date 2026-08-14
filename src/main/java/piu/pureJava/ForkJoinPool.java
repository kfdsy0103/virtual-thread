package piu.pureJava;

import java.util.List;
import java.util.Optional;

public class ForkJoinPool {

	public static void main(String[] args) {

		List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		// Single Stream
		Optional<Integer> optional = list.stream()
			.filter(integer -> {
				boolean b = integer % 2 == 0;
				return b;
			})
			.findAny();

		// 병렬 처리 (나는 스레드 만든 적 없는데? -> 내부적으로 ForkJoinPool에서 할당 -> ForkJoinPool은 Daemon Thread 할당해줌)
		Optional<Integer> optional_ = list.parallelStream()
			.filter(integer -> {
				System.out.println("i: " + integer + ", thread: " + Thread.currentThread() + ", daemon: " + Thread.currentThread().isDaemon());
				boolean b = integer % 2 == 0;
				return b;
			})
			.findAny();

		System.out.println(optional.get());
		System.out.println(optional_.get());
	}

}
