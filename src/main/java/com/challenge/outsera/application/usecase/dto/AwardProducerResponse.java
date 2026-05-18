package com.challenge.outsera.application.usecase.dto;

public record AwardProducerResponse(
        String producer,
        Integer interval,
        Integer previousWin,
        Integer followingWin
) {
}
