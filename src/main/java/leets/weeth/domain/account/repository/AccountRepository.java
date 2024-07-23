package leets.weeth.domain.account.repository;

import leets.weeth.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByCardinal(Integer cardinal);
}
