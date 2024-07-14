package leets.weeth.domain.attendance.repository;

import leets.weeth.domain.attendance.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
