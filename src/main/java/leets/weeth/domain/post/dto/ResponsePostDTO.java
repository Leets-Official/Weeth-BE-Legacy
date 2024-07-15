package leets.weeth.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import leets.weeth.domain.post.entity.Post;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ResponsePostDTO extends BaseEntity {

    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static ResponsePostDTO createResponsePostDTO(Post post) {
        return new ResponsePostDTO(post.getId(), post.getTitle(), post.getContent(),
                post.getCreatedAt(), post.getModifiedAt());
    }
}
