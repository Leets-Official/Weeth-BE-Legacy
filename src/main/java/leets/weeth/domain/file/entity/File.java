package leets.weeth.domain.file.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Getter
public class File {
    public File(String url) {
        this.url = url;
    }

    @Id //엔티티의 대푯값 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String url;


}
