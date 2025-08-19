package memo.model.dao;

import memo.model.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Long create(String username, String displayName);
    // 메서드 시그니처 -> 속성들을 직접 전달
    // 또다른 DTO로 엮어서...
    Optional<UserDTO> findById(Long userId); // PK
    Optional<UserDTO> findByUsername(String username); // 생성할 때 쓴 이름 -> 로그인
    List<UserDTO> findAll(int limit, int offset); // 50개 있다고 하면 5개씩 불러오게 ...
}