package com.challenge.outsera.application.usecase;

import com.challenge.outsera.application.provider.CsvMovieReaderProvider;
import com.challenge.outsera.application.provider.MovieProvider;
import com.challenge.outsera.domain.movie.MovieEntity;
import com.challenge.outsera.domain.producer.ProducerEntity;
import com.challenge.outsera.domain.studio.StudioEntity;
import com.challenge.outsera.infrastructure.producer.persistence.ProducerJpaEntity;
import com.challenge.outsera.infrastructure.producer.persistence.ProducerRepository;
import com.challenge.outsera.infrastructure.studio.persistence.StudioJpaEntity;
import com.challenge.outsera.infrastructure.studio.persistence.StudioRepository;
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

    private final StudioRepository studioRepository;
    private final ProducerRepository producerRepository;

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
            StudioEntity entity = studioRepository
                    .findByName(studio.name())
                    .orElseGet(
                        () -> studioRepository.save(StudioJpaEntity.toJpaEntity(studio))
                    )
                    .toEntity();

            resolvedStudios.add(entity);
        }
        return resolvedStudios;
    }

    private Set<ProducerEntity> resolveProducers(Set<ProducerEntity> producers) {
        Set<ProducerEntity> resolvedProducers = new HashSet<>();

        for (ProducerEntity producer : producers) {
            ProducerEntity entity = producerRepository
                    .findByName(producer.name())
                    .orElseGet(
                            () -> producerRepository.save(ProducerJpaEntity.toJpaEntity(producer))
                    )
                    .toEntity();

            resolvedProducers.add(entity);
        }
        return resolvedProducers;
    }
}
