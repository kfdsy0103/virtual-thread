package piu.config;

import org.springframework.boot.task.SimpleAsyncTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ExecutorConfig {

	// @Async 기본
	@Bean
	public SimpleAsyncTaskExecutor taskExecutor(SimpleAsyncTaskExecutorBuilder builder) {
		return builder.build();
	}

	// 플랫폼
	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(10);
		threadPoolTaskExecutor.setThreadNamePrefix("myExecutor-");
		// TODO: more settings...
		return threadPoolTaskExecutor;
	}
}
