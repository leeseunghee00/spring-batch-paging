package io.leeseunghee.jpajdbcpagingtest.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.leeseunghee.jpajdbcpagingtest.book.Book;
import io.leeseunghee.jpajdbcpagingtest.book.BookDto;
import io.leeseunghee.jpajdbcpagingtest.book.BookRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class JDBCPagingBatch {

	private final BookRepository bookRepository;
	private final DataSource dataSource;

	/**
	 * Reader
	 */
	@Bean
	@StepScope
	public JdbcPagingItemReader<BookDto> jdbcPagingItemReader() {

		JdbcPagingItemReader<BookDto> itemReader = new JdbcPagingItemReader<>();

		itemReader.setDataSource(dataSource);
		itemReader.setRowMapper((rs, rowNum) ->
			BookDto.builder()
				.id(rs.getLong("id"))
				.title(rs.getString("title"))
				.stock(rs.getInt("stock"))
				.status(rs.getString("status"))
				.build()
		);

		MySqlPagingQueryProvider query = new MySqlPagingQueryProvider();
		query.setSelectClause("SELECT id, title, stock, status");
		query.setFromClause("FROM book");
		query.setWhereClause("WHERE stock = 0");

		itemReader.setQueryProvider(query);

		return itemReader;
	}

	/**
	 * Processor
	 */
	@Bean
	@StepScope
	public ItemProcessor<BookDto, Book> jdbcPagingItemProcessor() {

		return dto -> {
			Book book = bookRepository.findBookById(dto.id());
			book.updateToSoldOut(book);
			return book;
		};
	}

	/**
	 * Writer
	 */
	@Bean
	@StepScope
	public ItemWriter<Book> jdbcPagingItemWriter() {
		return bookRepository::saveAll;
	}

}
