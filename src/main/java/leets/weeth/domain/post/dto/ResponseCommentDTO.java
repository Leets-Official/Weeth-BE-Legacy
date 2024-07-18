package leets.weeth.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import leets.weeth.domain.post.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ResponseCommentDTO {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String content;
    private LocalDateTime time;

    public static ResponseCommentDTO createResponseCommentDto(Comment comment) {
        return ResponseCommentDTO.builder()
                .id(comment.getId())
                .name(comment.getUser().getName())
                .content(comment.getContent())
                .time(comment.getTime())
                .build();
    }
}
