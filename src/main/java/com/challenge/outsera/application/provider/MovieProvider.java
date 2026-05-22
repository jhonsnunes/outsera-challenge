package com.challenge.outsera.application.provider;

import com.challenge.outsera.application.usecase.dto.ProducerWinYear;
import com.challenge.outsera.domain.movie.MovieEntity;
import com.challenge.outsera.infrastructure.movie.persistence.MovieJpaEntity;

import java.util.List;

public interface MovieProvider {
    List<MovieJpaEntity> saveAll(List<MovieEntity> movies);

    List<ProducerWinYear> findProducerWinYearsByWinnerMovies();
}
