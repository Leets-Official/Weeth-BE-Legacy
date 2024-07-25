package leets.weeth.domain.penalty.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
public class RequestPenalty {
    private Long userId;
    private String penaltyDescription;
}
