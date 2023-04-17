package org.infogain.domain.reward.service;

import lombok.RequiredArgsConstructor;
import org.infogain.domain.reward.model.RewardDetails;
import org.infogain.domain.transaction.model.Transaction;
import org.infogain.domain.transaction.service.TransactionService;
import org.infogain.domain.user.service.UserService;
import org.infogain.domain.util.DateTimeUtil;
import org.javatuples.Pair;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {

    private final UserService userService;
    private final TransactionService transactionService;
    private final DateTimeUtil dateTimeUtil;

    @Override
    public RewardDetails getRewardDetailsForUser(String userId) {
        userService.validateUserExists(userId);

        ZonedDateTime threeMonthsBeforeNow = dateTimeUtil.now().minusMonths(3);
        List<Transaction> transactions = transactionService.getTransactionsForUser(userId, threeMonthsBeforeNow);

        Map<Month, Integer> pointsMap = computePointsPerMonth(transactions);

        List<RewardDetails.PointsPerMonth> monthlyPoints = mapToList(pointsMap);

        return RewardDetails.builder()
                .userId(userId)
                .monthlyPoints(monthlyPoints)
                .totalPoints(computeTotalPoints(pointsMap))
                .build();
    }

    private Map<Month, Integer> computePointsPerMonth(List<Transaction> transactions) {
        return transactions.stream()
                .map(transaction -> Pair.with(transaction.getCreatedAt().getMonth(), computePoints(transaction.getAmount())))
                .collect(Collectors.groupingBy(Pair::getValue0, Collectors.summingInt(Pair::getValue1)));
    }

    private List<RewardDetails.PointsPerMonth> mapToList(Map<Month, Integer> pointsMap) {
        return pointsMap.entrySet().stream()
                .map(entry -> RewardDetails.PointsPerMonth.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private int computeTotalPoints(Map<Month, Integer> pointsMap) {
        return pointsMap.values().stream()
                .reduce(0, Integer::sum);
    }

    private int computePoints(double amount) {
        int points;
        int integerAmount = (int) amount;

        if (integerAmount - 100 > 0) {
            points = (integerAmount - 100) * 2 + 50;
        } else {
            points = integerAmount - 50;
        }

        return points;
    }
}
