package capstone.briefit.repository;

import capstone.briefit.domain.ArticleSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceRepository extends JpaRepository<ArticleSource, Long> {
    @Query(value = "SELECT s.press_company AS company, COUNT(*) AS cnt " +
            "FROM article_source s " +
            "GROUP BY s.press_company " +
            "ORDER BY cnt DESC", nativeQuery = true)
    List<Object[]> getSourceCompanyCategory();
}
