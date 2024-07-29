package leets.weeth.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageInfoDTO {
    private int totalPosts;
    public static PageInfoDTO createPageInfoDTO(int totalPosts){
        return PageInfoDTO.builder()
                .totalPosts(totalPosts)
                .build();
    }
}
