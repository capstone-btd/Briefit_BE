package capstone.briefit.repository;

import capstone.briefit.domain.Article;
import capstone.briefit.domain.User;
import capstone.briefit.domain.UserCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomRepository extends JpaRepository<UserCustom, Long> {
    void deleteAllByUser(User user);
     Optional<UserCustom> findByUserAndArticle(User user, Article article);
}
