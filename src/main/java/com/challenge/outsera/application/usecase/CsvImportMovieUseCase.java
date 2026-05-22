package com.challenge.outsera.application.usecase;

import com.challenge.outsera.application.provider.CsvMovieReaderProvider;
import com.challenge.outsera.application.provider.MovieProvider;
import com.challenge.outsera.application.provider.ProducerProvider;
import com.challenge.outsera.application.provider.StudioProvider;
import com.challenge.outsera.domain.movie.MovieEntity;
import com.challenge.outsera.domain.producer.ProducerEntity;
import com.challenge.outsera.domain.studio.StudioEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CsvImportMovieUseCase {
    private final CsvMovieReaderProvider csvMovieReaderProvider;
    private final MovieProvider movieProvider;
    private final StudioProvider studioProvider;
    private final ProducerProvider producerProvider;

    public void importMovies() {
        List<MovieEntity> movies = csvMovieReaderProvider.read();

        List<MovieEntity> resolvedMovies = new ArrayList<>();

        for (MovieEntity movie : movies) {
            Set<StudioEntity> resolvedStudios = resolveStudios(movie.studios());
            Set<ProducerEntity> resolvedProducers = resolveProducers(movie.producers());

            MovieEntity resolvedMovie = movie
                    .setStudios(resolvedStudios)
                    .setProducers(resolvedProducers);

            resolvedMovies.add(resolvedMovie);
        }

        movieProvider.saveAll(resolvedMovies);
    }

    private Set<StudioEntity> resolveStudios(Set<StudioEntity> studios) {
        Set<StudioEntity> resolvedStudios = new HashSet<>();

        for (StudioEntity studio : studios) {
            StudioEntity entity = studioProvider
                    .findByName(studio.name())
                    .orElseGet(() -> studioProvider.save(studio));

            resolvedStudios.add(entity);
        }
        return resolvedStudios;
    }

    private Set<ProducerEntity> resolveProducers(Set<ProducerEntity> producers) {
        Set<ProducerEntity> resolvedProducers = new HashSet<>();

        for (ProducerEntity producer : producers) {
            ProducerEntity entity = producerProvider
                    .findByName(producer.name())
                    .orElseGet(() -> producerProvider.save(producer));

            resolvedProducers.add(entity);
        }
        return resolvedProducers;
    }
}
