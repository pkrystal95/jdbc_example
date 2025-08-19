# JDBC
> JDBC 기반 DAO / DTO 패턴 이해하기

## DDL / DML 세팅
### 테이블이 존재한다면 삭제
```sql
-- 초기화
DROP TABLE IF EXISTS memo;
DROP TABLE IF EXISTS user_account;
```
### 테이블 생성
```sql
-- 사용자
CREATE TABLE user_account (
  user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  display_name VARCHAR(80) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 메모
CREATE TABLE memo (
  memo_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  title VARCHAR(200) NOT NULL,
  content TEXT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES user_account(user_id)
) ENGINE=InnoDB;
```
### 데이터 삽입
```sql
INSERT INTO user_account (username, display_name) VALUES
('krystal', '박수정'), ('gildong', '홍길동');

INSERT INTO memo (user_id, title, content) VALUES
(1, '첫 메모', '박수정의 첫 번째 메모'),
(1, '둘째 메모', '박수정의 두 번째 메모'),
(2, '밥의 메모', '홍길동의 유일한 메모');
```
### 데이터 조회(JOIN 예시)
```sql
SELECT m.memo_id, m.title, m.content, m.created_at,
       u.user_id, u.username, u.display_name
FROM memo m
JOIN user_account u ON u.user_id = m.user_id
ORDER BY m.memo_id DESC;
```

## DTO 정의
> DTO(Data Transfer Object) : 계층 간 데이터 전달 전용 불변 객체, JAVA Record로 정의한다.
> - 불변 + 명확한 목적, DB 행을 그대로 또는 JOIN 결과를 담아오기 좋다.

```java
import java.time.LocalDateTime;

// 단일 테이블 DTO
public record UserDTO(
  Long userId,
  String username,
  String displayName,
  LocalDateTime createdAt
) {}

public record MemoDTO(
  Long memoId,
  Long userId,
  String title,
  String content,
  LocalDateTime createdAt
) {}

// JOIN 결과 DTO (메모 + 작성자)
public record MemoWithAuthorDTO(
  Long memoId,
  String title,
  String content,
  LocalDateTime memoCreatedAt,
  Long authorId,
  String authorUsername,
  String authorDisplayName
) {}
```

## DAO 인터페이스
> DAO(Data Access Object): 영속 계층에 대한 추상 인터페이스. 구현체는 JDBC, 나중엔 JPA로 교체 가능.
>  - 나중에 JPA로 교체할 때, 서비스 계층 코드는 인터페이스만 의존하면 된다.

```java
import java.util.List;
import java.util.Optional;

public interface UserDAO {
  Long create(String username, String displayName);
  Optional<UserDTO> findById(Long userId);
  Optional<UserDTO> findByUsername(String username);
  List<UserDTO> findAll(int limit, int offset);
}

public interface MemoDAO {
  Long create(Long userId, String title, String content);
  Optional<MemoDTO> findById(Long memoId);
  List<MemoDTO> findByUserId(Long userId, int limit, int offset);
  List<MemoWithAuthorDTO> findAllWithAuthor(int limit, int offset);
  int deleteById(Long memoId);
}
```

## DB 연결 유틸
> - Aiven MySQL: ?allowPublicKeyRetrieval=true 권장

#### `.env` 파일 예시
```conf
DB_URL=jdbc:mysql://<host>:<port>/<db>?allowPublicKeyRetrieval=true
DB_USER=<user>
DB_PASSWORD=<password>
```
