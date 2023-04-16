package org.infogain.domain.reward.mapper;

import org.infogain.application.response.RewardDetailsResponse;
import org.infogain.domain.reward.model.RewardDetails;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class RewardMapperTest {

    private final RewardMapper rewardMapper = Mappers.getMapper(RewardMapper.class);

    @Test
    void should_toApi_mapObjectCorrectly() {
        // given
        final String userId = "userId";
        final int points = 100;

        RewardDetails rewardDetails = RewardDetails.builder()
                .userId(userId)
                .points(points)
                .build();

        // when
        RewardDetailsResponse rewardDetailsResponse = rewardMapper.toApi(rewardDetails);

        // then
        assertThat(rewardDetailsResponse.getUserId()).isEqualTo(userId);
        assertThat(rewardDetailsResponse.getPoints()).isEqualTo(points);
    }
}