package leets.weeth.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import leets.weeth.domain.post.entity.Post;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import leets.weeth.domain.post.entity.PostImage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ResponsePostDTO {

    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private LocalDateTime time;
    private List<String> imageUrls;

    public static ResponsePostDTO createResponsePostDTO(Post post) {
        return new ResponsePostDTO(post.getId(), post.getTitle(), post.getContent(),
                post.getTime(),  post.getPostImages().stream()
                .map(PostImage::getUrl)
                .collect(Collectors.toList()));
    }
}
