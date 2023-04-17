package org.infogain.domain.reward.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.Month;
import java.util.List;

@Value
@Builder(toBuilder = true)
public class RewardDetails {
    String userId;
    int totalPoints;
    List<PointsPerMonth> monthlyPoints;

    @Value
    @Builder
    @AllArgsConstructor(staticName = "of")
    public static class PointsPerMonth {
        Month month;
        int points;
    }
}
