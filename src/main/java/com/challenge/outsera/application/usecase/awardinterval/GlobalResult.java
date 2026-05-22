package com.challenge.outsera.application.usecase.awardinterval;

import com.challenge.outsera.application.usecase.dto.AwardProducerResponse;
import com.challenge.outsera.application.usecase.dto.AwardResponse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GlobalResult {
    private int globalMin = Integer.MAX_VALUE;
    private int globalMax = Integer.MIN_VALUE;
    private final List<AwardProducerResponse> minProducers = new ArrayList<>();
    private final List<AwardProducerResponse> maxProducers = new ArrayList<>();

    public void registerProducer(String producer, int winCount, IntervalBounds bounds) {
        if (winCount < 2 || !bounds.hasInterval()) {
            return;
        }

        if (bounds.minInterval() < globalMin) {
            globalMin = bounds.minInterval();
            minProducers.clear();
            minProducers.addAll(bounds.toMinResponses(producer));
        } else if (bounds.minInterval() == globalMin) {
            minProducers.addAll(bounds.toMinResponses(producer));
        }

        if (bounds.maxInterval() > globalMax) {
            globalMax = bounds.maxInterval();
            maxProducers.clear();
            maxProducers.addAll(bounds.toMaxResponses(producer));
        } else if (bounds.maxInterval() == globalMax) {
            maxProducers.addAll(bounds.toMaxResponses(producer));
        }
    }

    public boolean isEmpty() {
        return minProducers.isEmpty();
    }

    public AwardResponse toAwardResponse() {
        Comparator<AwardProducerResponse> ordering = Comparator
                .comparing(AwardProducerResponse::producer)
                .thenComparing(AwardProducerResponse::previousWin);

        minProducers.sort(ordering);
        maxProducers.sort(ordering);

        return new AwardResponse(
                List.copyOf(minProducers),
                List.copyOf(maxProducers)
        );
    }

    public static AwardResponse empty() {
        return new AwardResponse(List.of(), List.of());
    }
}
