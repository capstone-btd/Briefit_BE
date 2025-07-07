package capstone.briefit.dto;

import capstone.briefit.domain.enums.Tag;
import lombok.Builder;

import java.util.List;

public class UserResponseDTO {
    @Builder
    public record UserInfo(
        String nickname,
        String profileUrl,
        List<Tag> categories
    ){}
}
