package leets.weeth.domain.attendance.service;

import leets.weeth.domain.attendance.dto.RequestPenalty;
import leets.weeth.domain.attendance.entity.Penalty;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import leets.weeth.domain.attendance.repository.PenaltyRepository;

@Service
@RequiredArgsConstructor
public class PenaltyService {

    private final PenaltyRepository penaltyRepository;
    private final UserRepository userRepository;

    public void recordPenalty(RequestPenalty requestPenalty) throws BusinessLogicException {
        User user = userRepository.findById(requestPenalty.getUserId()).orElseThrow(() -> new BusinessLogicException("User not found"));
        Penalty penalty = new Penalty();
        penalty.setDescription(requestPenalty.getDescription());
        penalty.setUser(user);
        penaltyRepository.save(penalty);
    }
}
