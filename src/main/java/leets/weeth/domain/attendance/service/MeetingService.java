package leets.weeth.domain.attendance.service;

import leets.weeth.domain.attendance.dto.ResponseMeeting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import leets.weeth.domain.attendance.repository.MeetingRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;

    public List<ResponseMeeting> getAllMeetings() {
        return meetingRepository.findAll().stream()
                .map(meeting -> ResponseMeeting.builder()
                        .id(meeting.getId())
                        .name(meeting.getName())
                        .build())
                .collect(Collectors.toList());
    }
}
