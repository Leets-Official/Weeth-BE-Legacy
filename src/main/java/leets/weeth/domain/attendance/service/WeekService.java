package leets.weeth.domain.attendance.service;

import leets.weeth.domain.attendance.dto.AttendanceDTO;
import leets.weeth.domain.attendance.dto.ResponseWeekCode;
import leets.weeth.domain.attendance.entity.Week;
import leets.weeth.domain.attendance.mapper.AttendanceMapper;
import leets.weeth.domain.attendance.repository.WeekRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeekService {

    private final WeekRepository weekRepository;
    private final AttendanceMapper mapper;

    //임원이 총 주차 입력시 관련 정보 생성하는 메서드
    public void createWeeks(AttendanceDTO.Week dto) {
        Week week = mapper.fromWeek(dto);
        weekRepository.save(week);
    }

    //어드민이 주차별로 출석 코드를 조회하는 메서드
    @SneakyThrows
    public List<ResponseWeekCode> getWeekCode(Integer cardinal) {
        return weekRepository.findAllByCardinal(cardinal).stream()
                .map(ResponseWeekCode::new)
                .toList();
    }
}
