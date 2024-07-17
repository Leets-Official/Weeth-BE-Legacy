package leets.weeth.domain.attendance.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ResponseAttendanceSummary {
    private long totalEvents; // 총 모임 횟수
    private long totalAttendances; // 출석 횟수
    private long totalAbsences; // 결석 횟수
    private List<ResponseAttendance> attendanceDetails; // 상세 출석 정보 리스트
}
