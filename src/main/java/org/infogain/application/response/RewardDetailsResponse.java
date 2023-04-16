package org.infogain.application.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RewardDetailsResponse {
    String userId;
    int points;
}
