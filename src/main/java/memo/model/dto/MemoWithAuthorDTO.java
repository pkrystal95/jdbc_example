package memo.model.dto;

import java.time.LocalDateTime;

// JOIN + 최종적으로 JSP 출력될 결과
public record MemoWithAuthorDTO(
        Long memoId,
        String title,
        String content,
        LocalDateTime memoCreatedAt,
        Long authorId,
        String authorUsername,
        String authorDisplayName
) {}