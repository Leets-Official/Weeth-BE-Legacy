package leets.weeth.domain.account.entity;

import jakarta.persistence.*;
import leets.weeth.domain.file.entity.File;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Receipt extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_id")
    private Long id;

    private Integer amount;

    private String description;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<File> images;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
