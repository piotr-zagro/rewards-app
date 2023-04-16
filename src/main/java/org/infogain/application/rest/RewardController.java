package org.infogain.application.rest;

import lombok.RequiredArgsConstructor;
import org.infogain.application.response.RewardDetailsResponse;
import org.infogain.domain.reward.mapper.RewardMapper;
import org.infogain.domain.reward.model.RewardDetails;
import org.infogain.domain.reward.service.RewardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.REWARD_PATH)
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;
    private final RewardMapper rewardMapper;

    @GetMapping("/{userId}")
    public ResponseEntity<RewardDetailsResponse> rewardDetails(@PathVariable String userId) {
        RewardDetails rewardDetails = rewardService.getRewardDetailsForUser(userId);
        return ResponseEntity.ok(rewardMapper.toApi(rewardDetails));
    }
}
