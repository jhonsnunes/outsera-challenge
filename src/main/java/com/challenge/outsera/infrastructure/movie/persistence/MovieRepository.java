package com.challenge.outsera.infrastructure.movie.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<MovieJpaEntity, Long> {
    @Query("""
        SELECT DISTINCT m
        FROM
            Movie m
            LEFT JOIN FETCH m.producers
            LEFT JOIN FETCH m.studios
        WHERE
            m.winner = true
        ORDER BY m.year ASC
    """)
    List<MovieJpaEntity> findWinnerMovies();
}
