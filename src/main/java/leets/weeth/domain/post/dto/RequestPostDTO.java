package leets.weeth.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import leets.weeth.domain.post.entity.Post;
import leets.weeth.global.common.entity.BaseEntity;

public class RequestPostDTO extends BaseEntity {
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    public static PostDTO createReqeustPostDTO(Post post) {
        return new PostDTO(post.getId(), post.getTitle(), post.getContent());
    }
}