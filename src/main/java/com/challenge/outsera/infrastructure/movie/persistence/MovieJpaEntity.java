package com.challenge.outsera.infrastructure.movie.persistence;

import com.challenge.outsera.domain.movie.MovieEntity;
import com.challenge.outsera.domain.producer.ProducerEntity;
import com.challenge.outsera.domain.studio.StudioEntity;
import com.challenge.outsera.infrastructure.producer.persistence.ProducerJpaEntity;
import com.challenge.outsera.infrastructure.studio.persistence.StudioJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "Movie")
@Table(name = "movie")
public class MovieJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year_released", nullable = false)
    private Integer year;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToMany
    @JoinTable(
            name = "movie_studio",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "studio_id")
    )
    private Set<StudioJpaEntity> studios = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "movie_producer",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "producer_id")
    )
    private Set<ProducerJpaEntity> producers = new HashSet<>();

    @Column(name = "winner")
    private Boolean winner;

    public Long getId() {
        return id;
    }

    public Integer getYear() {
        return year;
    }

    public String getTitle() {
        return title;
    }

    public Set<StudioJpaEntity> getStudios() {
        return studios;
    }

    public Set<ProducerJpaEntity> getProducers() {
        return producers;
    }

    public Boolean getWinner() {
        return winner;
    }

    public static MovieJpaEntity toJpaEntity(MovieEntity movieEntity) {
        MovieJpaEntity jpaEntity = new MovieJpaEntity();

        jpaEntity.id = movieEntity.id();
        jpaEntity.year = movieEntity.year();
        jpaEntity.title = movieEntity.title();

        jpaEntity.studios = movieEntity
                .studios()
                .stream()
                .map(StudioJpaEntity::toJpaEntity)
                .collect(Collectors.toSet());

        jpaEntity.producers = movieEntity
                .producers()
                .stream()
                .map(ProducerJpaEntity::toJpaEntity)
                .collect(Collectors.toSet());

        jpaEntity.winner = movieEntity.winner();

        return jpaEntity;
    }

    public MovieEntity toEntity() {
        Set<StudioEntity> studioEntities = studios
                .stream()
                .map(StudioJpaEntity::toEntity)
                .collect(Collectors.toSet());

        Set<ProducerEntity> producerEntities = producers
                .stream()
                .map(ProducerJpaEntity::toEntity)
                .collect(Collectors.toSet());

        return new MovieEntity(
                id,
                year,
                title,
                studioEntities,
                producerEntities,
                winner
        );
    }
}
