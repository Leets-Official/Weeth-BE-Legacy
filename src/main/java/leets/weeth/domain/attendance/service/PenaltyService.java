package leets.weeth.domain.attendance.service;

import jakarta.persistence.EntityNotFoundException;
import leets.weeth.domain.attendance.dto.RequestPenalty;
import leets.weeth.domain.attendance.entity.Penalty;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import leets.weeth.domain.attendance.repository.PenaltyRepository;

@Service
@RequiredArgsConstructor
public class PenaltyService {

    private final PenaltyRepository penaltyRepository;
    private final UserRepository userRepository;

    public void recordPenalty(RequestPenalty requestPenalty, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
        Penalty penalty = new Penalty();
        penalty.setDescription(requestPenalty.getDescription());
        penalty.setUser(user);
        penaltyRepository.save(penalty);
    }
}
