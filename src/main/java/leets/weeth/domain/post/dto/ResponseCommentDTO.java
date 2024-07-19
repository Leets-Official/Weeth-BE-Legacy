package leets.weeth.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import leets.weeth.domain.post.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ResponseCommentDTO {
    private Long id;
    @NotBlank
    private String content;
    private LocalDateTime time;

    public static ResponseCommentDTO createResponseCommentDto(Comment comment) {
        return new ResponseCommentDTO(comment.getId(), comment.getContent(),
                comment.getTime());
    }
}
