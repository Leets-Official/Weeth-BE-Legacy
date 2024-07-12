package leets.weeth.domain.post.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import leets.weeth.domain.post.dto.PostDTO;
import leets.weeth.domain.user.entity.User;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Getter
@Table
public class Post extends BaseEntity {
    @Id //엔티티의 대푯값 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    public static Post createPost(PostDTO dto, User user){
        return new Post(
                dto.getId(),
                user,
                dto.getTitle(),
                dto.getContent()
        );
    }


    public void updatePost(PostDTO dto) {

        if (dto.getTitle()!= null)   //수정할 제목 데이터가 있다면
            this.title = dto.getTitle();
        if (dto.getContent() != null)  //수정할 본문 데이터가 있다면
            this.content = dto.getContent();
    }
}
