package leets.weeth.domain.user.entity;

import jakarta.persistence.*;
import leets.weeth.domain.user.converter.CardinalListConverter;
import leets.weeth.domain.user.dto.UserDTO;
import leets.weeth.domain.user.entity.enums.Department;
import leets.weeth.domain.user.entity.enums.Position;
import leets.weeth.domain.user.entity.enums.Role;
import leets.weeth.domain.user.entity.enums.Status;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.*;

import java.util.List;

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

    private String name;

    private String email;

    private String password;

    private String studentId;

    private String tel;

    @Enumerated(EnumType.STRING)
    private Position position;

    @Enumerated(EnumType.STRING)
    private Department department;

    @Convert(converter = CardinalListConverter.class)
    private List<Integer> cardinals;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;    // 수정: Redis로 옮길 예정

    @PrePersist
    public void init() {
        status = Status.WAITING;
        role = Role.USER;
    }

    public void updateRefreshToken(String updatedToken) {
        this.refreshToken = updatedToken;
    }

    public void leave() {
        this.status = Status.LEFT;
    }

    public void applyOB(Integer cardinal) {
        this.cardinals.add(cardinal);
    }

    public boolean isInactive() {
        return this.status != Status.ACTIVE;
    }

    public void update(UserDTO.Update dto) {
        this.name = dto.name();
        this.email = dto.email();
        this.password = dto.password();
        this.studentId = dto.studentId();
        this.tel = dto.tel();
        this.department = dto.department();
    }

}
