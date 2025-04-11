package io.leeseunghee.jpajdbcpagingtest.book;

import lombok.Builder;

public record BookDto(
	Long id,
	String title,
	Integer stock,
	String status
) {

	@Builder
	public BookDto(
		Long id,
		String title,
		Integer stock,
		String status
	) {
		this.id = id;
		this.title = title;
		this.stock = stock;
		this.status = status;
	}
}
