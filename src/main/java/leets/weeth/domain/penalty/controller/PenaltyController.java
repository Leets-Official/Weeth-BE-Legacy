package leets.weeth.domain.penalty.controller;

import leets.weeth.domain.penalty.dto.RequestPenalty;
import leets.weeth.domain.penalty.dto.ResponsePenalty;
import leets.weeth.domain.penalty.service.PenaltyService;
import leets.weeth.global.auth.annotation.CurrentUser;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/penalty")
public class PenaltyController {
    private final PenaltyService penaltyService;
    @GetMapping()
    public CommonResponse<List<ResponsePenalty>> showMyPenalty(@CurrentUser Long userId){
        List<ResponsePenalty> myPenalties= penaltyService.getMyPenalties(userId);
        return CommonResponse.createSuccess(myPenalties);
    }

}
