package leets.weeth.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import leets.weeth.domain.post.entity.Post;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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