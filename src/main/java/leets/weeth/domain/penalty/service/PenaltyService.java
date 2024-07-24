package leets.weeth.domain.penalty.service;

import leets.weeth.domain.penalty.dto.RequestPenalty;
import leets.weeth.domain.penalty.dto.ResponsePenalty;
import leets.weeth.domain.penalty.entity.Penalty;
import leets.weeth.domain.penalty.repository.PenaltyRepository;
import leets.weeth.domain.post.dto.ResponsePostDTO;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.PenaltyNotFoundException;
import leets.weeth.global.common.error.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class PenaltyService {
    private final PenaltyRepository penaltyRepository;
    private final UserRepository userRepository;
    public void assignPenalty(RequestPenalty requestPenalty){
        User userToBan = userRepository.findById(requestPenalty.getUserId()).orElseThrow(UserNotFoundException::new);
        penaltyRepository.save(Penalty.toEntity(requestPenalty, userToBan));
    }

    public List<ResponsePenalty> getMyPenalties(Long userId){
        User currentUser = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        List<Penalty> myPenalties = penaltyRepository.findByUserId(userId, Sort.by(Sort.Direction.ASC, "id"));
        return myPenalties.stream()
                .map(ResponsePenalty::createResponsePenaltyDTO) // Post -> ResponsePostDTO 변환
                .collect(Collectors.toList());
    }

    public void removePenalty(Long penaltyId) {
        Penalty penaltyToRemove = penaltyRepository.findById(penaltyId)
                .orElseThrow(PenaltyNotFoundException::new);
        penaltyRepository.delete(penaltyToRemove);
    }
}
