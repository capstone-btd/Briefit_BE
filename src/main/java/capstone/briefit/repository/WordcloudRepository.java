package capstone.briefit.repository;

import capstone.briefit.domain.Wordcloud;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordcloudRepository extends JpaRepository<Wordcloud, Long> {
}
