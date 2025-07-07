package capstone.briefit.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class WordcloudResponseDTO {
    @Builder
    public record WordcloudInfoDTO(
            LocalDateTime createdAt,
            List<Map<String, Object>> words
    ){}
}
