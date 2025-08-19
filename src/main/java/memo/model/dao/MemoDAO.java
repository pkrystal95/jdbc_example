
package memo.model.dao;

import memo.model.dto.MemoDTO;
import memo.model.dto.MemoWithAuthorDTO;

import java.util.List;
import java.util.Optional;

public interface MemoDAO {
    Long create(Long userId, String title, String content);
    Optional<MemoDTO> findById(Long userId); // PK
    List<MemoDTO> findByUserId(Long userId, int limit, int offset);
    List<MemoWithAuthorDTO> findAllWithAuthor(int limit, int offset);
    int deleteById(Long memoId);

}
