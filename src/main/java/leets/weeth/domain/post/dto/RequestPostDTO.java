package leets.weeth.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
public class RequestPostDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
}