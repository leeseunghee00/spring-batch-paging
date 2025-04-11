# spring-batch-paging
> 이 코드는 JPA 페이징과 JDBC 페이징을 비교하기 위한 간단한 배치 테스트용 예제입니다. <br />
> 테스트 목적: 재고량이 0인 책을 품절 상태로 변경하는 작업을 수행합니다.

<br />

### 개발 환경
- Java 17
- SpringBoot 3.4.4
- MySQL 8.3

<br />

### 테스트 데이터 생성
프로시저로 book 데이터 1,000개를 빠르게 삽입할 수 있습니다.
```sql
CREATE
    definer = root@`%` procedure insert_books()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE stock INT;

    WHILE i <= 1000 DO
        IF i <= 500 THEN
            SET stock = 0;
        ELSE
            SET stock = FLOOR(1 + (RAND() * 100)); -- 1~100 사이의 랜덤 값
        END IF;

        INSERT INTO book (status, stock, title)
        VALUES (
            '판매중',
            stock,
            CONCAT('title_', i)
        );

        SET i = i + 1;
    END WHILE;
END;
```

<br />

### 테스트 결과
평균 처리 시간과 처리량 비교해 보면, **JDBC 페이징 방식**은 JPA 페이징 방식보다 **약 3배 이상 빠른 속도와 높은 처리량을 보임을 확인할 수 있습니다.**


<img width="800" alt="image" src="https://github.com/user-attachments/assets/586e5f8e-8c2f-45cf-839a-6f51536dc7c4" />
<img width="800" alt="image" src="https://github.com/user-attachments/assets/460ac45a-bc89-42ef-91ee-08ead604f90c" />
