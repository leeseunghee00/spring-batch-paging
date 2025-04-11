package io.leeseunghee.jpajdbcpagingtest.book;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

	Book findBookById(Long id);
}
