package leets.weeth.domain.post.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import leets.weeth.domain.post.dto.RequestCommentDTO;
import leets.weeth.domain.user.entity.User;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;


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
    @Column
    private String content;
    LocalDateTime time;
    public static Comment createComment(RequestCommentDTO dto, Post post, User user){

        Comment newComment = new Comment(
                null,
                post,
                user,
                dto.getContent(),
                null
        );
        return newComment;
    }

    @PrePersist
    @PreUpdate
    public void setTime() {
        this.time = this.getModifiedAt() == null ? this.getCreatedAt() : this.getModifiedAt();
    }


}
