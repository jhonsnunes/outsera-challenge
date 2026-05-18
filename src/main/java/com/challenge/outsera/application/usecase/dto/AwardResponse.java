package com.challenge.outsera.application.usecase.dto;

import java.util.List;

public record AwardResponse(
    List<AwardProducerResponse> min,
    List<AwardProducerResponse> max
) {
}
