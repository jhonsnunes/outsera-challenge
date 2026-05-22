package com.challenge.outsera.infrastructure.api.controller;

import com.challenge.outsera.application.usecase.AwardIntervalUseCase;
import com.challenge.outsera.application.usecase.dto.AwardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "award-interval")
public class AwardController {
    private final AwardIntervalUseCase awardIntervalUseCase;

    @GetMapping
    @Operation(summary = "Intervalo de prêmios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Ocorreu um erro interno no servidor")
    })
    public AwardResponse getAwards() {
        return awardIntervalUseCase.execute();
    }
}
