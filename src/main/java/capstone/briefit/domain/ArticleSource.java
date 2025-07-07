package capstone.briefit.domain;

import capstone.briefit.domain.enums.Company;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 200, nullable = false)
    private String url;

    @Column(length = 10, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Company pressCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;
}
