package leets.weeth.domain.penalty.service;

import leets.weeth.domain.penalty.dto.RequestPenalty;
import leets.weeth.domain.penalty.dto.ResponsePenalty;
import leets.weeth.domain.penalty.entity.Penalty;
import leets.weeth.domain.penalty.repository.PenaltyRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.PenaltyNotFoundException;
import leets.weeth.global.common.error.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class PenaltyService {
    private final PenaltyRepository penaltyRepository;
    private final UserRepository userRepository;
    @Transactional(rollbackFor = Exception.class)
    public void assignPenalty(RequestPenalty requestPenalty){
        User userToBan = userRepository.findById(requestPenalty.getUserId()).orElseThrow(UserNotFoundException::new);
        Penalty penalty = penaltyRepository.save(Penalty.toEntity(requestPenalty, userToBan));
        userToBan.addPenalty(penalty);
    }

    public List<ResponsePenalty> getMyPenalties(Long userId){
        User currentUser = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        currentUser.getPenalties().sort(Comparator.comparing(Penalty::getId));

        return currentUser.getPenalties().stream()
                .map(ResponsePenalty::createResponsePenaltyDTO) // Post -> ResponsePostDTO 변환
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public void removePenalty(Long penaltyId) {
        Penalty penaltyToRemove = penaltyRepository.findById(penaltyId)
                .orElseThrow(PenaltyNotFoundException::new);
        penaltyRepository.delete(penaltyToRemove);
    }

    public List<ResponsePenalty> getAllPenaltiesSortedByUserId() {
        List<Penalty> allPenalties = penaltyRepository.findAll();
        return allPenalties.stream()
                .sorted(Comparator.comparing(Penalty-> Penalty.getUser().getId()))
                .map(ResponsePenalty::createResponsePenaltyDTO)
                .toList();
    }
}
