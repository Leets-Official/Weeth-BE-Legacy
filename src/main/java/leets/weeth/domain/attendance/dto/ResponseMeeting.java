package leets.weeth.domain.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseMeeting {
    private Long id;
    private String name;
    @NotBlank
    private String description;
    private String date;
}