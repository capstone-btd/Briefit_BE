package capstone.briefit.repository;

import capstone.briefit.domain.Article;
import capstone.briefit.domain.User;
import capstone.briefit.domain.UserScrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrapRepository extends JpaRepository<UserScrap, Long> {
    List<UserScrap> findAllByUser(User user);
    void deleteAllByUser(User user);
}
