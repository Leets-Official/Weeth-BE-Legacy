package leets.weeth.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import leets.weeth.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class PostDTO {

    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    public static PostDTO createPostDTO(Post post) {
        return new PostDTO(post.getId(), post.getTitle(), post.getContent());
    }

}
