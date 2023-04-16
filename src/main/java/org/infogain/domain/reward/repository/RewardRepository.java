package org.infogain.domain.reward.repository;

import org.infogain.domain.reward.model.RewardDetails;

public interface RewardRepository {
    RewardDetails getRewardDetails(String userId);
    RewardDetails saveRewardDetails(RewardDetails rewardDetails);
}
