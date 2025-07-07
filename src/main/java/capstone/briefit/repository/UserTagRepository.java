package capstone.briefit.repository;

import capstone.briefit.domain.User;
import capstone.briefit.domain.UserTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTagRepository extends JpaRepository<UserTag, Long> {
    void deleteAllByUser(User user);
}
