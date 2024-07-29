package leets.weeth.domain.user.repository;

import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByStudentId(String studentId);
    boolean existsByTel(String tel);

    boolean existsByEmailAndIdIsNot(String email, Long id);
    boolean existsByStudentIdAndIdIsNot(String studentId, Long id);
    boolean existsByTelAndIdIsNot(String tel, Long id);

    List<User> findAllByStatusOrderByName(Status status);

    Optional<User> findByRefreshToken(String refreshToken);
}
