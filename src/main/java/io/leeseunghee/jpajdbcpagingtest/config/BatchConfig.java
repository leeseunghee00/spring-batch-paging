package io.leeseunghee.jpajdbcpagingtest.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import ch.qos.logback.classic.util.DefaultJoranConfigurator;
import io.leeseunghee.jpajdbcpagingtest.batch.JDBCPagingBatch;
import io.leeseunghee.jpajdbcpagingtest.batch.JpaPagingBatch;
import io.leeseunghee.jpajdbcpagingtest.book.Book;
import io.leeseunghee.jpajdbcpagingtest.book.BookDto;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BatchConfig extends DefaultJoranConfigurator {

	private final JpaPagingBatch jpaPagingBatch;
	private final JDBCPagingBatch jdbcPagingBatch;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job jpaPagingBatchJob(JobRepository jobRepository) {

		return new JobBuilder("jpaPagingBatchJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(jpaPagingBatchStep(jobRepository))
			.build();
	}

	@Bean
	public Job jdbcPagingBatchJob(JobRepository jobRepository) {

		return new JobBuilder("jdbcPagingBatchJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(jdbcPagingBatchStep(jobRepository))
			.build();
	}

	@Bean
	public Step jpaPagingBatchStep(JobRepository jobRepository) {

		return new StepBuilder("jpaPagingBatchStep", jobRepository)
			.<Book, Book>chunk(100 ,transactionManager)
			.reader(jpaPagingBatch.jpaPagingItemReader())
			.processor(jpaPagingBatch.jpaPagingItemProcessor())
			.writer(jpaPagingBatch.jpaPagingItemWriter())
			.build();
	}

	@Bean
	public Step jdbcPagingBatchStep(JobRepository jobRepository) {

		return new StepBuilder("jdbcPagingBatchStep", jobRepository)
			.<BookDto, Book>chunk(100 ,transactionManager)
			.reader(jdbcPagingBatch.jdbcPagingItemReader())
			.processor(jdbcPagingBatch.jdbcPagingItemProcessor())
			.writer(jdbcPagingBatch.jdbcPagingItemWriter())
			.build();
	}
}
