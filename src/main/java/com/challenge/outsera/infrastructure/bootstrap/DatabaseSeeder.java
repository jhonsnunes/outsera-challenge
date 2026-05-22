package com.challenge.outsera.infrastructure.bootstrap;

import com.challenge.outsera.application.usecase.CsvImportMovieUseCase;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!test")
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final CsvImportMovieUseCase csvImportMovieUseCase;


    @Override
    public void run(String @NonNull ... args) {
        csvImportMovieUseCase.importMovies();
    }
}
