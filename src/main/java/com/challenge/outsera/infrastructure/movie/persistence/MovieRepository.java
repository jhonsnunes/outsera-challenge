package com.challenge.outsera.infrastructure.movie.persistence;

import com.challenge.outsera.application.usecase.dto.ProducerWinYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<MovieJpaEntity, Long> {

    @Query("""
        SELECT NEW com.challenge.outsera.application.usecase.dto.ProducerWinYear(p.name, m.year)
        FROM Movie m
        INNER JOIN m.producers p
        WHERE m.winner = true
        GROUP BY p.name, m.year
        ORDER BY p.name ASC, m.year ASC
    """)
    List<ProducerWinYear> findProducerWinYearsByWinnerMovies();
}
