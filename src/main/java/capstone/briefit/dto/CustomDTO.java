package capstone.briefit.dto;

import lombok.Builder;

import java.util.List;

public class CustomDTO {
    @Builder
    public record CustomInfoDTO(
            int startPoint,
            int endPoint,
            String highlightsColor,
            String highlightsFontColor,
            int highlightsFontSize,
            Boolean isBold
    ){}

    @Builder
    public record CustomizeRequestDTO(
            String backgroundColor,
            List<CustomInfoDTO> customInfos
    ){}
}
