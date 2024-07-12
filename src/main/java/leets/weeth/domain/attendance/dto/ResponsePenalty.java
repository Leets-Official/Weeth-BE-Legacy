package leets.weeth.domain.attendance.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsePenalty {
    private Long id;
    private String description;
    private Long userId;
}