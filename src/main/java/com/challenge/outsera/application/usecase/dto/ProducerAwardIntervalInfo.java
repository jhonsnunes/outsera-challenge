package com.challenge.outsera.application.usecase.dto;

import lombok.Getter;
import lombok.Setter;

public class ProducerAwardIntervalInfo {
    @Getter
    private final String name;

    @Setter
    @Getter
    private int minInterval = Integer.MAX_VALUE;

    @Setter
    @Getter
    private int minPrev = 0;

    @Setter
    @Getter
    private int minNext = 0;

    @Setter
    @Getter
    private int maxInterval = Integer.MIN_VALUE;

    @Setter
    @Getter
    private int maxPrev = 0;

    @Setter
    @Getter
    private int maxNext = 0;

    public ProducerAwardIntervalInfo(String name) {
        this.name = name;
    }
}

