package org.infogain.domain.reward.mapper;

import org.infogain.application.response.RewardDetailsResponse;
import org.infogain.domain.reward.model.RewardDetails;
import org.mapstruct.Mapper;

@Mapper
public interface RewardMapper {
    RewardDetailsResponse toApi(RewardDetails rewardDetails);
}
