package com.lotto.domain.resultchecker;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class WinnersRetriever {

    List<TicketResult> retrieveWinners(final List<Ticket> allTickets, final Set<Integer> winningNumbers) {
        if (allTickets == null || allTickets.isEmpty()) {
            return List.of();
        }

        return allTickets.stream()
                .map(ticket -> {
                    Set<Integer> numbersPlayer = ticket.numbers();
                    Set<Integer> matched = numbersPlayer.stream()
                            .filter(winningNumbers::contains)
                            .collect(Collectors.toSet());

                    int matchesCount = matched.size();
                    String category = categoryByMatches(matchesCount);

                    return TicketResult.builder()
                            .hash(ticket.hash())
                            .drawDate(ticket.drawDate())
                            .userNumbers(numbersPlayer)
                            .winningNumbers(winningNumbers)
                            .matchedNumbers(matched)
                            .matches(matchesCount)
                            .resultCategory(category)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private String categoryByMatches(int matches) {
        return switch (matches) {
            case 6 -> "JACKPOT";
            case 5 -> "FIVE_MATCH";
            case 4 -> "FOUR_MATCH";
            case 3 -> "THREE_MATCH";
            case 2 -> "TWO_MATCH";
            case 1 -> "ONE_MATCH";
            default -> "NO_MATCH";
        };
    }
}