package io.leeseunghee.jpajdbcpagingtest.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.leeseunghee.jpajdbcpagingtest.book.Book;
import io.leeseunghee.jpajdbcpagingtest.book.BookRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class JpaPagingBatch {

	private final BookRepository bookRepository;
	private final EntityManagerFactory emf;

	/**
	 * Reader
	 */
	@Bean
	@StepScope
	public JpaPagingItemReader<Book> jpaPagingItemReader() {

		JpaPagingItemReader<Book> itemReader = new JpaPagingItemReader<>();

		itemReader.setEntityManagerFactory(emf);
		itemReader.setQueryString("SELECT b FROM Book b WHERE b.stock = 0");
		itemReader.setPageSize(100);

		return itemReader;
	}

	/**
	 * Processor
	 */
	@Bean
	@StepScope
	public ItemProcessor<Book, Book> jpaPagingItemProcessor() {

		return book -> {
			book.updateToSoldOut(book);
			return book;
		};
	}

	/**
	 * Writer
	 */
	@Bean
	@StepScope
	public ItemWriter<Book> jpaPagingItemWriter() {
		return bookRepository::saveAll;
	}

}
