package piu.config;

import org.springframework.boot.task.SimpleAsyncTaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {

	// @Scheduled 기본
	@Bean
	public SimpleAsyncTaskScheduler taskScheduler(SimpleAsyncTaskSchedulerBuilder builder) {
		return builder.build();
	}

	// 플랫폼
	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setThreadNamePrefix("myScheduler-");
		threadPoolTaskScheduler.setPoolSize(10);
		// TODO: more settings...
		return threadPoolTaskScheduler;
	}
}
