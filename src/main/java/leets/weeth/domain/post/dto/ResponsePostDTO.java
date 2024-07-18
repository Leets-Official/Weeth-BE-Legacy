package leets.weeth.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import leets.weeth.domain.file.entity.File;
import leets.weeth.domain.post.entity.Post;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class ResponsePostDTO {

    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private LocalDateTime time;
    private List<File> fileUrls;

    public static ResponsePostDTO createResponsePostDTO(Post post) {
        return ResponsePostDTO.builder()
                .id(post.getId())
                .name(post.getUser().getName())
                .title(post.getTitle())
                .content(post.getContent())
                .time(post.getTime())
                .fileUrls(post.getFileUrls())
                .build();
    }
}
