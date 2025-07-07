package capstone.briefit.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrapCustom extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private int startPoint;

    @Column(columnDefinition = "TEXT", nullable = false)
    private int endPoint;

    @Column(columnDefinition = "VARCHAR(15) DEFAULT '노'", nullable = false)
    private String highlightsColor;

    @Column(columnDefinition = "VARCHAR(15) DEFAULT '검'", nullable = false)
    private String fontColor;

    @Column(columnDefinition = "INT DEFAULT 10", nullable = false)
    private int fontSize;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 1", nullable = false)
    private Boolean isBold;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrap_id")
    private UserScrap userScrap;
}
