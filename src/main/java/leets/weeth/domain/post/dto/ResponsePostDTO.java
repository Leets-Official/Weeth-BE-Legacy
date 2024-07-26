package leets.weeth.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import leets.weeth.domain.file.entity.File;
import leets.weeth.domain.post.entity.Post;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    @NotBlank
    private LocalDateTime time;
    private List<File> fileUrls;
    private List<ResponseCommentDTO> comments;
    @NotBlank
    private Long commentCount;


    public static ResponsePostDTO createResponsePostDTO(Post post) {
        return ResponsePostDTO.builder()
                .id(post.getId())
                .name(post.getUser().getName())
                .title(post.getTitle())
                .content(post.getContent())
                .time(post.getTime())
                .fileUrls(post.getFileUrls())
                .comments(post.getParentComments()
                        .stream()
                        .map(ResponseCommentDTO::createResponseCommentDto)
                        .collect(Collectors.toList()))
                .commentCount(Post.calculateTotalComments(post))
                .build();
    }
}
