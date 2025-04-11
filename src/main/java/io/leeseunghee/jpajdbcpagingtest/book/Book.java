package io.leeseunghee.jpajdbcpagingtest.book;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private Integer stock;

	private String status;

	@Builder
	public Book(
		String title,
		Integer stock
	) {
		this.title = title;
		this.stock = stock;
		this.status = "판매중";
	}

	public void updateToSoldOut(Book book) {
		if (book.status.equals("판매중")) {
			book.status = "품절";
		}
	}
}
