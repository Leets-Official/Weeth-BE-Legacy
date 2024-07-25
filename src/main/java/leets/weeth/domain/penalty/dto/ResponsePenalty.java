package leets.weeth.domain.penalty.dto;

import leets.weeth.domain.penalty.entity.Penalty;
import lombok.*;
import java.time.LocalDateTime;


@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class ResponsePenalty {
    private Long userId;
    private String userName;
    private Long penaltyId;
    private String penaltyDescription;
    private LocalDateTime time;
    private int penaltyCount;

    public static ResponsePenalty createResponsePenaltyDTO(Penalty penalty) {
        return ResponsePenalty.builder()
                .userId(penalty.getUser().getId())
                .userName(penalty.getUser().getName())
                .penaltyId(penalty.getId())
                .penaltyDescription(penalty.getPenaltyDescription())
                .time(penalty.getCreatedAt())
                .penaltyCount(penalty.getUser().getPenalties().size())
                .build();
    }
}
