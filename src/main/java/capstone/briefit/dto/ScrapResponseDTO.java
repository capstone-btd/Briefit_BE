package capstone.briefit.dto;

import capstone.briefit.domain.enums.Category;
import capstone.briefit.domain.enums.Company;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class ScrapResponseDTO {
    @Builder
    public record ScrapInfoDTO(
        Long scrapId,
        Boolean isCustomize,
        String title,
        String body,
        List<Category> categories,
        List<Company> pressCompanies,
        List<String> imgUrls,
        String backgroundColor,
        LocalDateTime createdAt
//        int fontSize,
//        Color backgroundColor,
//        Color fontColor
    ){}

    @Builder
    public record ScrapDetailInfoDTO(
            Long scrapId,
            String title,
            String body,
            List<Category> categories,
            List<SourceDTO.SourceInfoDTO> sources,
            List<String> imgUrls,
            String backgroundColor,
            int fontSize,
            String fontColor,
            LocalDateTime createdAt,
            List<CustomDTO.HighlightsInfoDTO> customs
    ){}

}
