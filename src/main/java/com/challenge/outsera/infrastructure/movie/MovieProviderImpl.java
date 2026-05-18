package com.challenge.outsera.infrastructure.movie;

import com.challenge.outsera.application.provider.MovieProvider;
import com.challenge.outsera.domain.movie.MovieEntity;
import com.challenge.outsera.infrastructure.movie.persistence.MovieJpaEntity;
import com.challenge.outsera.infrastructure.movie.persistence.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MovieProviderImpl implements MovieProvider {
    private final MovieRepository movieRepository;

    @Override
    public List<MovieJpaEntity> saveAll(List<MovieEntity> movies) {
        List<MovieJpaEntity> moviesJpaEntity = movies
                .stream()
                .map(MovieJpaEntity::toJpaEntity)
                .toList();

        return movieRepository.saveAll(moviesJpaEntity);
    }

    @Override
    public List<MovieEntity> findWinnerMovies() {
        return movieRepository.findWinnerMovies()
                .stream()
                .map(MovieJpaEntity::toEntity)
                .toList();
    }
}
