package leets.weeth.domain.account.repository;

import leets.weeth.domain.account.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
