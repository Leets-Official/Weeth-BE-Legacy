package leets.weeth.domain.account.entity;

import jakarta.persistence.*;
import leets.weeth.domain.account.dto.ReceiptDTO;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    private String description;

    private Integer total;

    private Integer cardinal;

    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Receipt> receipts = new ArrayList<>();

    public void spend(Receipt receipt) {
        this.total -= receipt.getAmount();
        this.receipts.add(receipt);
    }

    public void cancel(Receipt receipt) {
        this.total += receipt.getAmount();
        this.receipts.remove(receipt);
    }
}
