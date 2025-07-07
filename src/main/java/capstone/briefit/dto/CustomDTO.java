package capstone.briefit.dto;

import lombok.Builder;

import java.util.List;

public class CustomDTO {
    @Builder
    public record HighlightsInfoDTO(
            int startPoint,
            int endPoint,
            String highlightsColor,
            String highlightsFontColor,
            int highlightsFontSize,
            Boolean isBold
    ){}

    @Builder
    public record CustomizeRequestInfoDTO(
            String backgroundColor,
            List<HighlightsInfoDTO> highlightsInfos
    ){}
}
