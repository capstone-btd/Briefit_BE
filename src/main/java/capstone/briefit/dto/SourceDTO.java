package capstone.briefit.dto;

import capstone.briefit.domain.enums.Company;
import lombok.Builder;

public class SourceDTO {
    @Builder
    public record SourceInfoDTO(
        String sourceTitle,
        Company pressCompany,
        String url
    ){}
}
