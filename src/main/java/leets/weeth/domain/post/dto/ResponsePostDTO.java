package leets.weeth.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import leets.weeth.domain.post.entity.Post;
import leets.weeth.global.common.entity.BaseEntity;

public class ResponsePostDTO extends BaseEntity {

    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    public static PostDTO createResponsePostDTO(Post post) {
        return new PostDTO(post.getId(), post.getTitle(), post.getContent());
    }
}
