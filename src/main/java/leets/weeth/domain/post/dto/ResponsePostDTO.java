package leets.weeth.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import leets.weeth.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import leets.weeth.domain.post.entity.PostFile;

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
    private List<String> fileUrls;

    public static ResponsePostDTO createResponsePostDTO(Post post) {
        return new ResponsePostDTO(post.getId(), post.getTitle(), post.getContent(),
                post.getTime(), post.getPostFiles().stream()
                .map(PostFile::getUrl)
                .collect(Collectors.toList()));
    }
}
