package org.infogain.domain.reward.service;

import lombok.RequiredArgsConstructor;
import org.infogain.domain.reward.model.RewardDetails;
import org.infogain.domain.reward.repository.RewardRepository;
import org.infogain.domain.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {

    private final RewardRepository rewardRepository;
    private final UserService userService;

    @Override
    public RewardDetails getRewardDetailsForUser(String userId) {
        userService.validateUserExists(userId);
        return rewardRepository.getRewardDetails(userId);
    }

    @Override
    public RewardDetails addPointsToUser(String userId, int pointsToAdd) {
        RewardDetails rewardDetails = getRewardDetailsForUser(userId);
        RewardDetails newRewardDetails = rewardDetails.toBuilder()
                .points(rewardDetails.getPoints() + pointsToAdd)
                .build();
        return rewardRepository.saveRewardDetails(newRewardDetails);
    }

    @Override
    public int calculatePoints(double amount) {
        int points;
        int integerAmount = (int) amount;

        if(integerAmount - 100 > 0) {
            points = (integerAmount - 100) * 2 + 50;
        } else {
            points = integerAmount - 50;
        }

        return points;
    }
}
