package com.challenge.outsera.domain.movie;

import com.challenge.outsera.domain.producer.ProducerEntity;
import com.challenge.outsera.domain.studio.StudioEntity;

import java.util.Set;

public record MovieEntity(
    Long id,
    Integer year,
    String title,
    Set<StudioEntity> studios,
    Set<ProducerEntity> producers,
    Boolean winner
) {
    public MovieEntity setStudios(Set<StudioEntity> studios) {
        return new MovieEntity(id, year, title, studios, producers, winner);
    }

    public MovieEntity setProducers(Set<ProducerEntity> producers) {
        return new MovieEntity(id, year, title, studios, producers, winner);
    }
}
