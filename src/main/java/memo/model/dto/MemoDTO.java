package memo.model.dto;

import java.time.LocalDateTime;

public record MemoDTO(
        Long memoId, // BIGINT
        Long userId, // MemoDTO PK -> FK
        String title,
        String content,
        LocalDateTime createdAt
) {}