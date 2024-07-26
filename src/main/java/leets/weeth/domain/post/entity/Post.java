package leets.weeth.domain.post.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import leets.weeth.domain.file.entity.File;
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

    @OneToMany(orphanRemoval = true)
    @JsonManagedReference
    private List<Comment> parentComments = new ArrayList<>();

    // 총 댓글 개수 : 댓글 + 대댓글
    private Long totalComments = 0L;

    LocalDateTime time;
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<File> fileUrls = new ArrayList<>();

    public static Post createPost(RequestPostDTO dto, User user, List<File> urls){

        return Post.builder()
                .user(user)
                .title(dto.getTitle())
                .content(dto.getContent())
                .time(null)
                .fileUrls(urls)
                .totalComments(0L)
                .build();
    }

    public void updatePost(RequestPostDTO dto, List<File> newUrls) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.fileUrls.clear(); // 기존 파일 제거
        this.fileUrls.addAll(newUrls); // 새로운 url 추가
    }

    public void calculateTotalComments() {
        Long totalComments = parentComments.stream()
                .mapToLong(this::countCommentsRecursively)
                .sum();
        this.totalComments = totalComments;
    }

    private Long countCommentsRecursively(Comment comment) {
        if (comment.getChildren() == null) {
            return 1L; // 현재 댓글만 카운트
        }
        return 1L + comment.getChildren().stream()
                .mapToLong(this::countCommentsRecursively)
                .sum();
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
