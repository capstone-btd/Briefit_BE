package capstone.briefit.repository;

import capstone.briefit.domain.CustomInfo;
import capstone.briefit.domain.UserCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomInfoRepository extends JpaRepository<CustomInfo, Long> {
    void deleteAllByUserCustom(UserCustom userCustom);
    List<CustomInfo> findAllByUserCustom(UserCustom userCustom);
}
