
package memo.model.dto;

import java.time.LocalDateTime;

public record UserDTO(
        Long userId, // BIGINT
        String username,
        String displayName,
        LocalDateTime createdAt
) {}
