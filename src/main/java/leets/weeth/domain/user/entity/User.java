package leets.weeth.domain.user.entity;

import jakarta.persistence.*;
import leets.weeth.domain.attendance.entity.Attendance;
import leets.weeth.domain.user.converter.CardinalListConverter;
import leets.weeth.domain.user.dto.UserDTO;
import leets.weeth.domain.user.entity.enums.Department;
import leets.weeth.domain.user.entity.enums.Position;
import leets.weeth.domain.user.entity.enums.Role;
import leets.weeth.domain.user.entity.enums.Status;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
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

    private String refreshToken;

    private Integer attendanceCount;

    private Integer attendanceRate;

    @OneToMany(mappedBy = "user")
    private List<Attendance> attendances;

    @PrePersist
    public void init() {
        status = Status.WAITING;
        role = Role.USER;
        attendanceCount = 0;
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

    public void update(UserDTO.Update dto, PasswordEncoder passwordEncoder) {
        this.name = dto.name();
        this.email = dto.email();
        this.password = passwordEncoder.encode(dto.password());
        this.studentId = dto.studentId();
        this.tel = dto.tel();
        this.department = dto.department();
    }

    public void accept() {
        this.status = Status.ACTIVE;
    }

    public void ban() {
        this.status = Status.BANNED;
    }

    public void update(String role) {
        this.role = Role.valueOf(role);
    }

    public Integer getCurrentCardinal() {
        return this.cardinals.stream().max(Integer::compareTo)
                .orElse(0);
    }

    public void attend(Attendance attendance) {
        ++this.attendanceCount;
        this.attendanceRate = (this.attendanceCount * 100) / getCurrentWeek();
        this.attendances.add(attendance);
    }

    private Integer getCurrentWeek() {
        return (int) this.getAttendances().stream()
                .map(Attendance::getWeek)
                .filter(week -> week.getDate().isEqual(LocalDate.now()) || week.getDate().isBefore(LocalDate.now()))
                .count();
    }
}
