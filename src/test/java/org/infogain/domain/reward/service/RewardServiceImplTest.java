package org.infogain.domain.reward.service;

import org.infogain.domain.reward.model.RewardDetails;
import org.infogain.domain.transaction.model.Transaction;
import org.infogain.domain.transaction.service.TransactionService;
import org.infogain.domain.user.service.UserService;
import org.infogain.domain.util.DateTimeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RewardServiceImplTest {

    private static final String USER_ID = "userId";

    @Mock
    private UserService userService;
    @Mock
    private TransactionService transactionService;
    @Mock
    private DateTimeUtil dateTimeUtil;

    private RewardServiceImpl rewardServiceImpl;

    @BeforeEach
    void setUp() {
        rewardServiceImpl = new RewardServiceImpl(userService, transactionService, dateTimeUtil);
    }

    @Test
    void should_getRewardDetailsForUser_returnRewardDetails() {
        // given
        ZonedDateTime fixedNow = ZonedDateTime.of(2023, 4, 20, 1, 1, 1, 1, ZoneOffset.UTC);

        List<Transaction> transactions = List.of(
                buildTransaction(fixedNow, 120.0),
                buildTransaction(fixedNow, 100.1),
                buildTransaction(fixedNow.minusMonths(1), 60.0),
                buildTransaction(fixedNow.minusMonths(1), 100.9),
                buildTransaction(fixedNow.minusMonths(2), 50.0)
        );

        when(dateTimeUtil.now()).thenReturn(fixedNow);
        when(transactionService.getTransactionsForUser(USER_ID, fixedNow.minusMonths(3))).thenReturn(transactions);

        // when
        RewardDetails actualDetails = rewardServiceImpl.getRewardDetailsForUser(USER_ID);

        // then
        verify(userService, times(1)).validateUserExists(USER_ID);

        assertThat(actualDetails.getTotalPoints()).isEqualTo(200);
        assertThat(actualDetails.getMonthlyPoints()).contains(
                RewardDetails.PointsPerMonth.of(Month.APRIL, 140),
                RewardDetails.PointsPerMonth.of(Month.MARCH, 60),
                RewardDetails.PointsPerMonth.of(Month.FEBRUARY, 0)
        );
    }

    private Transaction buildTransaction(ZonedDateTime createdAt, double amount) {
        return Transaction.builder()
                .transactionId("trId")
                .userId(USER_ID)
                .createdAt(createdAt)
                .amount(amount)
                .build();
    }
}