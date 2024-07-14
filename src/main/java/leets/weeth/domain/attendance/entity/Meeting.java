package leets.weeth.domain.attendance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Meeting_id;
    private String name;
    private String description;
    private String date;
    public Meeting() {}

    public Meeting(String name, String description, String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }
}
