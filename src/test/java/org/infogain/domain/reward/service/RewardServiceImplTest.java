package org.infogain.domain.reward.service;

import org.infogain.domain.reward.model.RewardDetails;
import org.infogain.domain.reward.repository.RewardRepository;
import org.infogain.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RewardServiceImplTest {

    private static final String USER_ID = "userId";

    @Mock
    private RewardRepository rewardRepository;
    @Mock
    private UserService userService;

    private RewardServiceImpl rewardServiceImpl;

    @BeforeEach
    void setUp() {
        rewardServiceImpl = new RewardServiceImpl(rewardRepository, userService);
    }

    @Test
    void should_getRewardDetailsForUser_returnRewardDetails() {
        // given
        RewardDetails expectedDetails = buildRewardDetails(100);
        when(rewardRepository.getRewardDetails(USER_ID)).thenReturn(expectedDetails);

        // when
        RewardDetails actualDetails = rewardServiceImpl.getRewardDetailsForUser(USER_ID);

        // then
        verify(userService, times(1)).validateUserExists(USER_ID);

        assertThat(actualDetails).isEqualTo(expectedDetails);
    }

    @Test
    void should_addPointsToUser_addPoints_whenPointsToAddIsPositive() {
        // given
        RewardDetails oldRewardDetails = buildRewardDetails(80);
        RewardDetails newRewardDetails = buildRewardDetails(100);
        when(rewardRepository.getRewardDetails(USER_ID)).thenReturn(oldRewardDetails);
        when(rewardRepository.saveRewardDetails(newRewardDetails)).thenReturn(newRewardDetails);

        // when
        RewardDetails actualDetails = rewardServiceImpl.addPointsToUser(USER_ID, 20);

        // then
        verify(userService, times(1)).validateUserExists(USER_ID);

        assertThat(actualDetails).isEqualTo(newRewardDetails);
    }

    @Test
    void should_addPointsToUser_subtractPoints_whenPointsToAddIsNegative() {
        // given
        RewardDetails oldRewardDetails = buildRewardDetails(100);
        RewardDetails newRewardDetails = buildRewardDetails(80);
        when(rewardRepository.getRewardDetails(USER_ID)).thenReturn(oldRewardDetails);
        when(rewardRepository.saveRewardDetails(newRewardDetails)).thenReturn(newRewardDetails);

        // when
        RewardDetails actualDetails = rewardServiceImpl.addPointsToUser(USER_ID, -20);

        // then
        verify(userService, times(1)).validateUserExists(USER_ID);

        assertThat(actualDetails).isEqualTo(newRewardDetails);
    }

    @ParameterizedTest
    @CsvSource({
            "120, 90",
            "60, 10",
            "50, 0",
            "100, 50",
            "100.1, 50",
            "100.9, 50"
    })
    void should_calculatePoints_returnCorrectAmountOfPoints(double amount, int expectedPoints) {
        // given // when
        int actualPoints = rewardServiceImpl.calculatePoints(amount);

        // then
        assertThat(actualPoints).isEqualTo(expectedPoints);
    }

    private RewardDetails buildRewardDetails(int points) {
        return RewardDetails.builder()
                .userId(USER_ID)
                .points(points)
                .build();
    }
}