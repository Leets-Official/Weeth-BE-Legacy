package leets.weeth.domain.penalty.entity;

import com.amazonaws.Request;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import leets.weeth.domain.penalty.dto.RequestPenalty;
import leets.weeth.domain.user.entity.User;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Getter
@Table
public class Penalty extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "penalty_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotEmpty
    private String penaltyDescription;

    public static Penalty toEntity(RequestPenalty dto, User user){
        return Penalty.builder()
                .user(user)
                .penaltyDescription(dto.getPenaltyDescription())
                .build();
    }
}
