package leets.weeth.domain.post.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import leets.weeth.domain.post.dto.RequestCommentDTO;
import leets.weeth.domain.user.entity.User;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static jakarta.persistence.FetchType.LAZY;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Getter
@Table
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotEmpty
    private String content;

    @ColumnDefault("FALSE")
    @Column(nullable = false)
    private Boolean isDeleted;

    @ManyToOne(fetch = LAZY)
    @JsonManagedReference
    @JoinColumn(name = "parent_id")
    private Comment parent; //null일 경우 최상위 댓글

    @JsonBackReference
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    LocalDateTime time;
    public static Comment createComment(RequestCommentDTO dto, Post post, User user){
        return Comment.builder()
                .id(null)
                .post(post)
                .user(user)
                .content(dto.getContent())
                .isDeleted(false)
                .time(null)
                .build();
    }

    public void updateComment(RequestCommentDTO dto) {
        this.content = dto.getContent();
    }

    @PrePersist
    @PreUpdate
    public void setTime() {
        this.time = this.getModifiedAt() == null ? this.getCreatedAt() : this.getModifiedAt();
    }

    public void markAsDeleted(){
        this.isDeleted = true;
        this.content = "삭제된 댓글입니다.";
    }

    public void setParentComment(Comment parentComment) {
        this.parent = parentComment;
    }
}
