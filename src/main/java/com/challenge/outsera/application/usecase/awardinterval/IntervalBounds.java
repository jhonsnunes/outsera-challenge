package com.challenge.outsera.application.usecase.awardinterval;

import com.challenge.outsera.application.usecase.dto.AwardProducerResponse;

import java.util.ArrayList;
import java.util.List;

public class IntervalBounds {
    private int minInterval = Integer.MAX_VALUE;
    private int maxInterval = Integer.MIN_VALUE;
    private final List<IntervalOccurrence> minOccurrences = new ArrayList<>();
    private final List<IntervalOccurrence> maxOccurrences = new ArrayList<>();

    public void register(int interval, int previousYear, int followingYear) {
        IntervalOccurrence occurrence = new IntervalOccurrence(interval, previousYear, followingYear);

        if (interval < minInterval) {
            minInterval = interval;
            minOccurrences.clear();
            minOccurrences.add(occurrence);
        } else if (interval == minInterval) {
            minOccurrences.add(occurrence);
        }

        if (interval > maxInterval) {
            maxInterval = interval;
            maxOccurrences.clear();
            maxOccurrences.add(occurrence);
        } else if (interval == maxInterval) {
            maxOccurrences.add(occurrence);
        }
    }

    public boolean hasInterval() {
        return !minOccurrences.isEmpty();
    }

    public int minInterval() {
        return minInterval;
    }

    public int maxInterval() {
        return maxInterval;
    }

    public List<AwardProducerResponse> toMinResponses(String producer) {
        return minOccurrences.stream()
                .map(occurrence -> occurrence.toResponse(producer))
                .toList();
    }

    public List<AwardProducerResponse> toMaxResponses(String producer) {
        return maxOccurrences.stream()
                .map(occurrence -> occurrence.toResponse(producer))
                .toList();
    }
}
