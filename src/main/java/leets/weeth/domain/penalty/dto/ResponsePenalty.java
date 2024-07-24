package leets.weeth.domain.penalty.dto;

import leets.weeth.domain.penalty.entity.Penalty;
import lombok.*;

import java.time.LocalDate;


@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class ResponsePenalty {
    private Long userId;
    private String penaltyDescription;

    public static ResponsePenalty createResponsePenaltyDTO(Penalty penalty) {
        return ResponsePenalty.builder()
                .userId(penalty.getId())
                .penaltyDescription(penalty.getPenaltyDescription())
                .build();
    }
}
