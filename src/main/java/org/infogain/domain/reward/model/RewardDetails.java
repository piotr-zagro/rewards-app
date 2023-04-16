package org.infogain.domain.reward.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class RewardDetails {
    String userId;
    int points;
}
