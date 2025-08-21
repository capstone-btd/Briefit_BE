package capstone.briefit.domain;

import capstone.briefit.domain.enums.Provider;
import capstone.briefit.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(length = 10)
    private String nickname;

    @Setter
    @Column(length = 200)
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private Provider provider;

    @Column(length = 100, nullable = false)
    private String providedId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'ACTIVE'", nullable = false)
    private Status status;

    private LocalDateTime inactiveDate;

    @OneToMany(mappedBy = "user")
    @Setter
    private List<UserTag> userTags = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserScrap> userScraps = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserCustom> userCustoms = new ArrayList<>();
}
