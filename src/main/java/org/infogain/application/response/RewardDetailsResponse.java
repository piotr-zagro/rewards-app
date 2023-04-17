package org.infogain.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class RewardDetailsResponse {
    String userId;
    int totalPoints;
    List<PointsPerMonth> monthlyPoints;

    @Value
    @Builder
    @AllArgsConstructor(staticName = "of")
    public static class PointsPerMonth {
        String month;
        int points;
    }
}
