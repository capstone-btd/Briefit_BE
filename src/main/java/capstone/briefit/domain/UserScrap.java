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
public class UserScrap extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(columnDefinition = "VARCHAR(15) DEFAULT '흰'", nullable = false)
    private String backgroundColor;

    @Column(columnDefinition = "VARCHAR(15) DEFAULT '검'", nullable = false)
    private String fontColor;

    @Column(columnDefinition = "INT DEFAULT 10", nullable = false)
    private int fontSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @OneToMany(mappedBy = "userScrap")
    @Getter
    private List<ScrapCustom> scrapCustoms = new ArrayList<>();
}
