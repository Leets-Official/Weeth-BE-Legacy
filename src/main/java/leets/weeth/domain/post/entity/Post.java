package leets.weeth.domain.post.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import leets.weeth.domain.post.dto.RequestPostDTO;
import leets.weeth.domain.user.entity.User;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    LocalDateTime time;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @OrderBy("id asc")
    private List<PostFile> postFiles;

    public static Post createPost(RequestPostDTO dto, User user){

        Post newPost = new Post(
                null,
                user,
                dto.getTitle(),
                dto.getContent(),
                null,
                new ArrayList<>()
        );
        return newPost;
    }

    @PrePersist
    @PreUpdate
    public void setTime() {
        this.time = this.getModifiedAt() == null ? this.getCreatedAt() : this.getModifiedAt();
    }

    public void updatePost(RequestPostDTO dto) {

        if (dto.getTitle()!= null)   //수정할 제목 데이터가 있다면
            this.title = dto.getTitle();
        if (dto.getContent() != null)  //수정할 본문 데이터가 있다면
            this.content = dto.getContent();
        this.setTime();
    }
}
