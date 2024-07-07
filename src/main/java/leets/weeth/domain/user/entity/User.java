package leets.weeth.domain.user.entity;

import jakarta.persistence.*;
import leets.weeth.domain.user.entity.enums.Role;
import leets.weeth.domain.user.entity.enums.Status;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Status status;

    public void updateRefreshToken(String updatedToken) {
        this.refreshToken = updatedToken;
    }

    public void leave() {
        this.status = Status.LEFT;
    }
}
