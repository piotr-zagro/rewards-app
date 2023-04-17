package org.infogain.domain.reward.mapper;

import org.infogain.application.response.RewardDetailsResponse;
import org.infogain.domain.reward.model.RewardDetails;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RewardMapperTest {

    private final RewardMapper rewardMapper = Mappers.getMapper(RewardMapper.class);

    @Test
    void should_toApi_mapObjectCorrectly() {
        // given
        final String userId = "userId";
        final int totalPoints = 100;
        RewardDetails.PointsPerMonth pointsPerMonth = RewardDetails.PointsPerMonth.of(Month.APRIL, 90);

        RewardDetails rewardDetails = RewardDetails.builder()
                .userId(userId)
                .totalPoints(totalPoints)
                .monthlyPoints(List.of(pointsPerMonth))
                .build();

        // when
        RewardDetailsResponse rewardDetailsResponse = rewardMapper.toApi(rewardDetails);

        // then
        assertThat(rewardDetailsResponse.getUserId()).isEqualTo(userId);
        assertThat(rewardDetailsResponse.getTotalPoints()).isEqualTo(totalPoints);
        assertThat(rewardDetailsResponse.getMonthlyPoints()).hasSize(1);
        assertThat(rewardDetailsResponse.getMonthlyPoints().get(0).getMonth()).isEqualTo(Month.APRIL.name());
        assertThat(rewardDetailsResponse.getMonthlyPoints().get(0).getPoints()).isEqualTo(90);
    }
}