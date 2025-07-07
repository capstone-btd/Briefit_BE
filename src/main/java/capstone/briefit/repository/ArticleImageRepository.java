package capstone.briefit.repository;

import capstone.briefit.domain.ArticleImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleImageRepository extends JpaRepository<ArticleImage, Long> {
}
