package capstone.briefit.dto;

import capstone.briefit.domain.enums.Category;
import capstone.briefit.domain.enums.Company;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleResponseDTO {
    @Builder
    public record ArticleInfoDTO(
            Long articleId,
            Long scrapId,
            Boolean isCustomize,
            String title,
            String body,
            List<Category> categories,
            List<Company> pressCompanies,
            List<String> imgUrls,
            LocalDateTime createdAt
    ){}

    @Builder
    public record ArticleDetailInfoDTO(
            String title,
            String body,
            List<Category> categories,
            List<SourceDTO.SourceInfoDTO> sources,
            List<String> imgUrls,
            LocalDateTime createdAt
    ){}
}
