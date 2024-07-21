package leets.weeth.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import leets.weeth.domain.post.entity.Comment;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseCommentDTO {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String content;
    private List<ResponseCommentDTO> children;
    private LocalDateTime time;

    public static ResponseCommentDTO createResponseCommentDto(Comment comment) {
        return ResponseCommentDTO.builder()
                .id(comment.getId())
                .name(comment.getUser().getName())
                .content(comment.getContent())
                .time(comment.getTime())
                .children(comment.getChildren().stream()
                        .map(ResponseCommentDTO::createResponseCommentDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
