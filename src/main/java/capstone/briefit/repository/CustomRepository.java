package capstone.briefit.repository;

import capstone.briefit.domain.ScrapCustom;
import capstone.briefit.domain.UserScrap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomRepository extends JpaRepository<ScrapCustom, Long> {
    void deleteAllByUserScrap(UserScrap userScrap);
}
