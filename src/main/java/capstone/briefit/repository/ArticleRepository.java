package capstone.briefit.repository;

import capstone.briefit.domain.Article;
import capstone.briefit.domain.ArticleCategory;
import capstone.briefit.domain.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query(value = "SELECT DISTINCT a.* FROM article a, article_category ac WHERE a.id = ac.article_id and ac.category = :category", nativeQuery = true)
    List<Article> findByCategory(@Param("category") String category);

    @Query(value = "SELECT DISTINCT a.* FROM article a, article_category ac WHERE a.id = ac.article_id and ac.category in :tags", nativeQuery = true)
    List<Article> findByTags(@Param("tags") List<String> tags);

    @Query(value = "SELECT DISTINCT a.* FROM article a, article_category ac WHERE a.id = ac.article_id and ac.category in :tags and ac.category = :category", nativeQuery = true)
    List<Article> findByTagsAndCategory(@Param("tags") List<String> tags, @Param("category") String category);

    List<Article> findByTitleContainingOrBodyContaining(String s1, String s2);
}
