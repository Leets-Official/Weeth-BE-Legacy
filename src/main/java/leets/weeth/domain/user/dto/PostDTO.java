package leets.weeth.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import leets.weeth.domain.user.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class PostDTO {
    @NotBlank
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    public PostDTO createPostDTO(Post post) {
        return new PostDTO(post.getId(), post.getTitle(), post.getContent());
    }

}
