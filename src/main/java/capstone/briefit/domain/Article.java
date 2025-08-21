package capstone.briefit.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @OneToMany(mappedBy = "article")
    @Getter
    private List<ArticleCategory> articleCategories = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    @Getter
    private List<ArticleImage> articleImages = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    @Getter
    private List<ArticleSource> articleSources = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    @Getter
    private List<UserCustom> userCustoms = new ArrayList<>();
}
