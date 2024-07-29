package leets.weeth.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ResponsePostDTO {
    private List<PostDTO> posts;
    private PageInfoDTO pageInfoDTO;

    public static ResponsePostDTO createResponsePostDTO(List<PostDTO> posts, PageInfoDTO pageInfoDTO){
        return ResponsePostDTO.builder()
                .posts(posts)
                .pageInfoDTO(pageInfoDTO)
                .build();
    }
}
