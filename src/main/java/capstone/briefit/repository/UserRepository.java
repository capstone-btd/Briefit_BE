package capstone.briefit.repository;

import capstone.briefit.domain.User;
import capstone.briefit.domain.UserScrap;
import capstone.briefit.domain.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProvidedId(String providedId);
}
