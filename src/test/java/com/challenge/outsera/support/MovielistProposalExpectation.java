package com.challenge.outsera.support;

import com.challenge.outsera.application.usecase.dto.AwardProducerResponse;

import java.util.List;

/**
 * Valores esperados derivados de src/main/resources/movielist.csv
 * Qualquer alteração no arquivo que mude o resultado da API deve atualizar este snapshot e o checksum abaixo.
 */
public final class MovielistProposalExpectation {

    public static final String CSV_SHA256 = "df5e4a3ae2c7aeaf67d0cbf1ad39407d07d4b8ebaf538a0af436292a0c7855b1";

    public static final List<AwardProducerResponse> MIN = List.of(
            new AwardProducerResponse("Joel Silver", 1, 1990, 1991)
    );

    public static final List<AwardProducerResponse> MAX = List.of(
            new AwardProducerResponse("Matthew Vaughn", 13, 2002, 2015)
    );

    private MovielistProposalExpectation() {
    }
}
