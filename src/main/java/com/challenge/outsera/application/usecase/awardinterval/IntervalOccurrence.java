package com.challenge.outsera.application.usecase.awardinterval;

import com.challenge.outsera.application.usecase.dto.AwardProducerResponse;

public record IntervalOccurrence(int interval, int previousWin, int followingWin) {

    public AwardProducerResponse toResponse(String producer) {
        return new AwardProducerResponse(producer, interval, previousWin, followingWin);
    }
}
