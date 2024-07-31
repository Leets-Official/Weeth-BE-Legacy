package leets.weeth.domain.event.attendanceEvent.service;

import leets.weeth.domain.attendance.dto.ResponseWeekCode;
import leets.weeth.domain.attendance.entity.Week;
import leets.weeth.domain.attendance.repository.WeekRepository;
import leets.weeth.domain.event.attendanceEvent.dto.RequestAttendanceEvent;
import leets.weeth.domain.event.attendanceEvent.dto.ResponseAttendanceEvent;
import leets.weeth.domain.event.attendanceEvent.mapper.AttendanceEventMapper;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.entity.enums.Type;
import leets.weeth.domain.event.repository.EventRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.InvalidInputDateException;
import leets.weeth.global.common.error.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceEventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final WeekRepository weekRepository;
    private final AttendanceEventMapper mapper;

    /*
     * 수정, 조회, 삭제는 관리자가 직접 수행
     * 출석일정을 통해 출석정보를 생성하기 때문에 출석일정 수정시 문제가 일어날 가능성 증가
     * 관리자가 DB 단에서 직접 수정, 삭제할 예정
     * AttendanceEventController로 온 요청의 경우 TYPE.ATTENDANCE로 생성
     */

    // 출석 일정 생성
    @Transactional
    public void createAttendanceEvent(RequestAttendanceEvent requestDto, Long userId) throws InvalidInputDateException {
        // 기간 입력 검증
        validateDateRange(requestDto.startDateTime(), requestDto.endDateTime());

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        eventRepository.save(mapper.fromAttendanceEventDto(requestDto, user));
    }

    @Transactional(readOnly = true)
    public List<ResponseAttendanceEvent> getAttendanceEvents() {
        // 이벤트를 타입별로 정렬된 목록으로 조회
        List<Event> events = eventRepository.findAllByType(Type.ATTENDANCE, Sort.by(Sort.Direction.ASC, "startDateTime"));
        // 모든 주간 데이터를 조회
        List<Week> weeks = weekRepository.findAll();

        // 중복된 날짜를 가진 주간 데이터가 있을 경우, 하나의 주간 데이터만 유지
        Map<LocalDate, Week> weekMap = weeks.stream()
                .collect(Collectors.toMap(
                        Week::getDate,
                        week -> week,
                        (existing, replacement) -> existing // 중복 키가 발생할 경우 기존 값을 유지
                ));

        // 이벤트 목록을 순회하며 주간 데이터와 매핑하여 DTO로 변환
        return events.stream()
                .map(event -> {
                    Week matchingWeek = weekMap.get(event.getStartDateTime().toLocalDate());
                    return mapper.toAttendanceEventDto(event, matchingWeek);
                })
                .filter(Objects::nonNull)
                .toList();
    }

    // 시작 날짜가 종료 날짜 보다 느린지 검증
    private void validateDateRange(LocalDateTime start, LocalDateTime end) throws InvalidInputDateException {
        if (start.isAfter(end)) {
            throw new InvalidInputDateException();
        }
    }
}
