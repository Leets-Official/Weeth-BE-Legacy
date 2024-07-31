package leets.weeth.domain.post.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import leets.weeth.domain.file.converter.FileListConverter;
import leets.weeth.domain.post.dto.RequestPostDTO;
import leets.weeth.domain.user.entity.User;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
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
    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    @JsonManagedReference
    private List<Comment> parentComments = new ArrayList<>();

    LocalDateTime time;

    @Convert(converter = FileListConverter.class)
    private List<String> files = new ArrayList<>();

    public static Post createPost(RequestPostDTO dto, User user, List<String> files){

        return Post.builder()
                .user(user)
                .title(dto.getTitle())
                .content(dto.getContent())
                .time(null)
                .files(files)
                .build();
    }

    public void updatePost(RequestPostDTO dto, List<String> files) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.files = files;
    }

    public static Long calculateTotalComments(Post post) {
        Long totalComments = post.parentComments.stream()
                .mapToLong(post::countCommentsRecursively)
                .sum();
        return totalComments;
    }

    private Long countCommentsRecursively(Comment comment) {
        // 부모 댓글이 삭제된 경우
        if(comment.getIsDeleted()){
            if (comment.getChildren() == null) {
                return 0L; // 0개
            }
            return comment.getChildren().stream()
                    .mapToLong(this::countCommentsRecursively)
                    .sum(); // 자식 댓글만 카운트 
        }
        else{
            if (comment.getChildren() == null) {
                return 1L; // 현재 댓글만 카운트
            }
            return 1L + comment.getChildren().stream()
                    .mapToLong(this::countCommentsRecursively)
                    .sum(); // 현재 및 자식 댓글 카운트
        }
    }

    @PrePersist
    @PreUpdate
    public void setTime() {
        this.time = this.getModifiedAt() == null ? this.getCreatedAt() : this.getModifiedAt();
    }

    public void addComment(Comment newComment) {
        this.parentComments.add(newComment);
    }
}
