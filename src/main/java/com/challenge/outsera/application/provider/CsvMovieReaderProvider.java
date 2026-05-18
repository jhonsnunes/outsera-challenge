package com.challenge.outsera.application.provider;

import com.challenge.outsera.domain.movie.MovieEntity;

import java.util.List;

public interface CsvMovieReaderProvider {
    List<MovieEntity> read();
}
