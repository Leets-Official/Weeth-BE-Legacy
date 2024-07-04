package leets.weeth.domain.entity;

import jakarta.persistence.*;
import leets.weeth.domain.entity.enums.Role;
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

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    public void updateRefreshToken(String updatedToken) {
        this.refreshToken = updatedToken;
    }
}
