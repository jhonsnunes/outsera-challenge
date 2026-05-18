package com.challenge.outsera;

import com.challenge.outsera.application.usecase.AwardIntervalUseCase;
import com.challenge.outsera.application.usecase.dto.AwardProducerResponse;
import com.challenge.outsera.application.usecase.dto.AwardResponse;
import com.challenge.outsera.domain.movie.MovieEntity;
import com.challenge.outsera.domain.producer.ProducerEntity;
import com.challenge.outsera.domain.studio.StudioEntity;
import com.challenge.outsera.infrastructure.movie.persistence.MovieJpaEntity;
import com.challenge.outsera.infrastructure.movie.persistence.MovieRepository;
import com.challenge.outsera.infrastructure.producer.persistence.ProducerJpaEntity;
import com.challenge.outsera.infrastructure.producer.persistence.ProducerRepository;
import com.challenge.outsera.infrastructure.studio.persistence.StudioJpaEntity;
import com.challenge.outsera.infrastructure.studio.persistence.StudioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AwardIntervalIntegrationTest {

    @Autowired
    private AwardIntervalUseCase awardIntervalUseCase;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private StudioRepository studioRepository;

    @BeforeEach
    void setUp() {
        movieRepository.deleteAll();
        producerRepository.deleteAll();
        studioRepository.deleteAll();
    }

    @Test
    void shouldReturnMultipleMinAndMaxProducersWithCorrectIntervals() {
        StudioEntity testStudio = studioRepository
                .save(
                        StudioJpaEntity.toJpaEntity(new StudioEntity(null, "Test Studio"))
                )
                .toEntity();

        ProducerEntity minProducer1 = producerRepository
                .save(
                        ProducerJpaEntity.toJpaEntity(new ProducerEntity(null, "MinProducer1"))
                )
                .toEntity();

        ProducerEntity minProducer2 = producerRepository
                .save(
                        ProducerJpaEntity.toJpaEntity(new ProducerEntity(null, "MinProducer2"))
                )
                .toEntity();

        ProducerEntity maxProducer1 = producerRepository
                .save(
                        ProducerJpaEntity.toJpaEntity(new ProducerEntity(null, "MaxProducer1"))
                )
                .toEntity();

        ProducerEntity maxProducer2 = producerRepository
                .save(
                        ProducerJpaEntity.toJpaEntity(new ProducerEntity(null, "MaxProducer2"))
                )
                .toEntity();

        createWinnerMovie(2017, "MinProducer1 Film A", testStudio, minProducer1);
        createWinnerMovie(2018, "MinProducer1 Film B", testStudio, minProducer1);

        createWinnerMovie(2016, "MinProducer2 Film A", testStudio, minProducer2);
        createWinnerMovie(2017, "MinProducer2 Film B", testStudio, minProducer2);

        createWinnerMovie(1980, "MaxProducer1 Film A", testStudio, maxProducer1);
        createWinnerMovie(2018, "MaxProducer1 Film B", testStudio, maxProducer1);

        createWinnerMovie(1981, "MaxProducer2 Film A",  testStudio, maxProducer2);
        createWinnerMovie(2019, "MaxProducer2 Film B",  testStudio, maxProducer2);


        AwardResponse response = awardIntervalUseCase.execute();

        assertThat(response.min()).isNotEmpty();
        assertThat(response.min()).hasSize(2);

        List<AwardProducerResponse> minProducers = response.min();

        assertThat(minProducers)
                .extracting(AwardProducerResponse::producer)
                .containsExactlyInAnyOrder("MinProducer1", "MinProducer2");

        for (AwardProducerResponse producer : minProducers) {

            assertThat(producer.interval()).isEqualTo(1);

            if ("MinProducer1".equals(producer.producer())) {
                assertThat(producer.previousWin()).isEqualTo(2017);
                assertThat(producer.followingWin()).isEqualTo(2018);
            } else if ("MinProducer2".equals(producer.producer())) {
                assertThat(producer.previousWin()).isEqualTo(2016);
                assertThat(producer.followingWin()).isEqualTo(2017);
            }
        }
        assertThat(response.max()).isNotEmpty();
        assertThat(response.max()).hasSize(2);

        List<AwardProducerResponse> maxProducers = response.max();

        assertThat(maxProducers)
                .extracting(AwardProducerResponse::producer)
                .containsExactlyInAnyOrder("MaxProducer1", "MaxProducer2");

        for (AwardProducerResponse producer : maxProducers) {
            assertThat(producer.interval()).isEqualTo(38);
            if ("MaxProducer1".equals(producer.producer())) {
                assertThat(producer.previousWin()).isEqualTo(1980);
                assertThat(producer.followingWin()).isEqualTo(2018);
            } else if ("MaxProducer2".equals(producer.producer())) {
                assertThat(producer.previousWin()).isEqualTo(1981);
                assertThat(producer.followingWin()).isEqualTo(2019);
            }
        }
    }

    @Test
    void shouldReturnEmptyResultsWhenNoWinnerMovies() {
        AwardResponse response = awardIntervalUseCase.execute();

        assertThat(response.min()).isEmpty();
        assertThat(response.max()).isEmpty();
    }

    @Test
    void shouldIgnoreProducersWithSingleWinnerMovie() {
        StudioEntity testStudio = studioRepository
                .save(
                        StudioJpaEntity.toJpaEntity(new StudioEntity(null, "Test Studio"))
                )
                .toEntity();

        ProducerEntity singleWinProducer = producerRepository
                .save(
                        ProducerJpaEntity.toJpaEntity(new ProducerEntity(null, "SingleWinProducer"))
                )
                .toEntity();

        ProducerEntity multiWinProducer = producerRepository
                .save(
                        ProducerJpaEntity.toJpaEntity(new ProducerEntity(null, "MultiWinProducer"))
                )
                .toEntity();

        createWinnerMovie(2000, "Single Win Film", testStudio, singleWinProducer);

        createWinnerMovie(2000, "Multi Win Film A", testStudio, multiWinProducer);
        createWinnerMovie(2005, "Multi Win Film B", testStudio, multiWinProducer);

        AwardResponse response = awardIntervalUseCase.execute();

        assertThat(response.min()).hasSize(1);
        assertThat(response.min().getFirst().producer()).isEqualTo("MultiWinProducer");
        assertThat(response.min().getFirst().interval()).isEqualTo(5);

        assertThat(response.max()).hasSize(1);
        assertThat(response.max().getFirst().producer()).isEqualTo("MultiWinProducer");
        assertThat(response.max().getFirst().interval()).isEqualTo(5);
    }

    @Test
    void shouldCalculateCorrectlyWithMultipleIntervalsPerProducer() {
        StudioEntity testStudio = studioRepository
                .save(
                    StudioJpaEntity.toJpaEntity(new StudioEntity(null, "Test Studio"))
                )
                .toEntity();

        ProducerEntity producer = producerRepository
                .save(
                    ProducerJpaEntity.toJpaEntity(new ProducerEntity(null, "MultiIntervalProducer"))
                )
                .toEntity();

        createWinnerMovie(2000, "Film 1", testStudio, producer);
        createWinnerMovie(2005, "Film 2", testStudio, producer);
        createWinnerMovie(2015, "Film 3", testStudio, producer);
        createWinnerMovie(2018, "Film 4", testStudio, producer);

        AwardResponse response = awardIntervalUseCase.execute();

        assertThat(response.min()).hasSize(1);
        assertThat(response.min().getFirst().interval()).isEqualTo(3);
        assertThat(response.min().getFirst().previousWin()).isEqualTo(2015);
        assertThat(response.min().getFirst().followingWin()).isEqualTo(2018);

        assertThat(response.max()).hasSize(1);
        assertThat(response.max().getFirst().interval()).isEqualTo(10);
        assertThat(response.max().getFirst().previousWin()).isEqualTo(2005);
        assertThat(response.max().getFirst().followingWin()).isEqualTo(2015);
    }

    @Test
    void shouldIgnoreNonWinnerMovies() {
        StudioEntity testStudio = studioRepository
                .save(
                    StudioJpaEntity.toJpaEntity(new StudioEntity(null, "Test Studio"))
                )
                .toEntity();

        ProducerEntity producer = producerRepository
                .save(
                    ProducerJpaEntity.toJpaEntity(new ProducerEntity(null, "MixedProducer"))
                )
                .toEntity();

        createWinnerMovie(2000, "Winner Film 1", testStudio, producer);

        MovieEntity nonWinnerMovie = new MovieEntity(
                null,
                2001,
                "Non-Winner Film",
                Set.of(testStudio),
                Set.of(producer),
                false
        );
        movieRepository.save(MovieJpaEntity.toJpaEntity(nonWinnerMovie));

        createWinnerMovie(2002, "Winner Film 2", testStudio, producer);

        AwardResponse response = awardIntervalUseCase.execute();

        assertThat(response.min()).hasSize(1);
        assertThat(response.min().getFirst().interval()).isEqualTo(2);
        assertThat(response.min().getFirst().previousWin()).isEqualTo(2000);
        assertThat(response.min().getFirst().followingWin()).isEqualTo(2002);
    }

    private void createWinnerMovie(
            Integer year,
            String title,
            StudioEntity studio,
            ProducerEntity producer
    ) {
        MovieEntity movie = new MovieEntity(
                null,
                year,
                title,
                Set.of(studio),
                Set.of(producer),
                true
        );
        movieRepository.save(MovieJpaEntity.toJpaEntity(movie));
    }
}

