package leets.weeth.domain.attendance.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseStatistics {
    private long totalAttendances;
    private long totalAttended;
    private double attendanceRate;

    public ResponseStatistics() {
    }

    public ResponseStatistics(long totalAttendances, long totalAttended, double attendanceRate) {
        this.totalAttendances = totalAttendances;
        this.totalAttended = totalAttended;
        this.attendanceRate = attendanceRate;
    }

    public void setTotalAttendances(long totalAttendances) {
        this.totalAttendances = totalAttendances;
    }

    public void setTotalAttended(long totalAttended) {
        this.totalAttended = totalAttended;
    }

    public void setAttendanceRate(double attendanceRate) {
        this.attendanceRate = attendanceRate;
    }
}
