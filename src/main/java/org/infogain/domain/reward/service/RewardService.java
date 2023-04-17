package org.infogain.domain.reward.service;

import org.infogain.domain.reward.model.RewardDetails;

public interface RewardService {
    RewardDetails getRewardDetailsForUser(String userId);
}
