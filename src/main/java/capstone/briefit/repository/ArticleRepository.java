package capstone.briefit.repository;

import capstone.briefit.domain.Article;
import capstone.briefit.domain.ArticleCategory;
import capstone.briefit.domain.enums.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query(value = "SELECT DISTINCT a.* FROM article a, article_category ac, article_source as s WHERE a.id = ac.article_id and a.id = s.article_id and (:category = '전체' or ac.category = :category) and (:company = '전체' or s.press_company = :company)", nativeQuery = true)
    List<Article> findByCategoryAndCompany(@Param("category") String category, @Param("company") String company);

    @Query(value =
            "SELECT COUNT(a.id) " +
                    "FROM article a " +
                    "WHERE " +
                    "(:category = '전체' OR EXISTS (" +
                    "   SELECT 1 FROM article_category ac " +
                    "   WHERE ac.article_id = a.id AND ac.category = :category" +
                    ")) " +
                    "AND (:company = '전체' OR EXISTS (" +
                    "   SELECT 1 FROM article_source s " +
                    "   WHERE s.article_id = a.id AND s.press_company = :company" +
                    "))",
            nativeQuery = true)
    long countByCategoryAndCompany(@Param("category") String category,
                                   @Param("company") String company);

    @Query(value = "SELECT DISTINCT a.* FROM article a, article_category ac, article_source as s WHERE a.id = ac.article_id and a.id = s.article_id and ac.category in :tags and (:company = '전체' or s.press_company = :company)", nativeQuery = true)
    List<Article> findByTagsAndCompany(@Param("tags") List<String> tags, @Param("company") String company);

    @Query(value =
            "SELECT COUNT(a.id) " +
                    "FROM article a " +
                    "WHERE EXISTS (" +
                    "   SELECT 1 FROM article_category ac " +
                    "   WHERE ac.article_id = a.id AND ac.category IN :tags" +
                    ") " +
                    "AND (:company = '전체' OR EXISTS (" +
                    "   SELECT 1 FROM article_source s " +
                    "   WHERE s.article_id = a.id AND s.press_company = :company" +
                    "))",
            nativeQuery = true)
    long countByTagsAndCompany(@Param("tags") List<String> tags,
                               @Param("company") String company);

    @Query(value = "SELECT DISTINCT a.* FROM article a, article_category ac, article_source as s WHERE a.id = ac.article_id and a.id = s.article_id and ac.category in :tags and ac.category = :category and (:company = '전체' or s.press_company = :company)", nativeQuery = true)
    List<Article> findByTagsAndCategoryAndCompany(@Param("tags") List<String> tags, @Param("category") String category, @Param("company") String company);

    @Query(value =
            "SELECT COUNT(a.id) " +
                    "FROM article a " +
                    "WHERE EXISTS (" +
                    "   SELECT 1 FROM article_category ac " +
                    "   WHERE ac.article_id = a.id " +
                    "   AND ac.category IN :tags " +
                    "   AND ac.category = :category" +
                    ") " +
                    "AND (:company = '전체' OR EXISTS (" +
                    "   SELECT 1 FROM article_source s " +
                    "   WHERE s.article_id = a.id AND s.press_company = :company" +
                    "))",
            nativeQuery = true)
    long countByTagsAndCategoryAndCompany(@Param("tags") List<String> tags,
                                          @Param("category") String category,
                                          @Param("company") String company);

    @Query(value = "SELECT DISTINCT a.* " +
            "FROM article a, article_source s " +
            "WHERE (a.id = s.article_id) " +
            "AND (REPLACE(a.title, ' ', '') LIKE %:keyword% " +
            "OR REPLACE(a.body, ' ', '') LIKE %:keyword%) " +
            "AND (:company = '전체' OR s.press_company = :company)",
            nativeQuery = true)
    List<Article> findByKeywordAndCompany(@Param("keyword") String keyword,
                                          @Param("company") String company);
//                                          Pageable pageable);
    @Query(value =
            "SELECT COUNT(a.id) " +
                    "FROM article a " +
                    "WHERE (REPLACE(a.title, ' ', '') LIKE %:keyword% " +
                    "   OR REPLACE(a.body, ' ', '') LIKE %:keyword%) " +
                    "AND (:company = '전체' OR EXISTS (" +
                    "   SELECT 1 FROM article_source s " +
                    "   WHERE s.article_id = a.id AND s.press_company = :company" +
                    "))",
            nativeQuery = true)
    long countByKeywordAndCompany(@Param("keyword") String keyword,
                                  @Param("company") String company);
}
