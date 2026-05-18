package com.challenge.outsera.infrastructure.csv;

import com.challenge.outsera.application.provider.CsvMovieReaderProvider;
import com.challenge.outsera.domain.movie.MovieEntity;
import com.challenge.outsera.domain.producer.ProducerEntity;
import com.challenge.outsera.domain.studio.StudioEntity;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CsvMovieReaderImpl implements CsvMovieReaderProvider {

    public List<MovieEntity> read() {

        try (
                InputStream inputStream =
                        getClass()
                                .getClassLoader()
                                .getResourceAsStream("movielist.csv");

                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(inputStream)
                        )
        ) {

            CSVFormat csvFormat =
                    CSVFormat.DEFAULT
                            .builder()
                            .setDelimiter(';')
                            .setHeader()
                            .setSkipHeaderRecord(true)
                            .get();

            CSVParser csvParser = CSVParser.parse(reader, csvFormat);

            List<MovieEntity> movies = new ArrayList<>();

            for (CSVRecord record : csvParser) {
                Integer year = Integer.parseInt(record.get("year"));
                String title = record.get("title");

                Set<StudioEntity> studios = parseStudios(record.get("studios"));
                Set<ProducerEntity> producers = parseProducers(record.get("producers"));

                boolean winner = "yes".equalsIgnoreCase(record.get("winner"));

                movies.add(
                        new MovieEntity(
                                null,
                                year,
                                title,
                                studios,
                                producers,
                                winner
                        )
                );
            }

            return movies;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Set<StudioEntity> parseStudios (String value){
        String normalized = value.replace(" and ", ",");

        return Arrays
                .stream(normalized.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(name -> new StudioEntity(null, name))
                .collect(Collectors.toSet());
    }

    private Set<ProducerEntity> parseProducers (String value){
        String normalized = value.replace(" and ", ",");

        return Arrays
                .stream(normalized.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(name -> new ProducerEntity(null, name))
                .collect(Collectors.toSet());
    }
}