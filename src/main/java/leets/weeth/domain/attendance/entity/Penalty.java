package leets.weeth.domain.attendance.entity;
import jakarta.persistence.*;
import leets.weeth.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Penalty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Penalty_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String description;
}
