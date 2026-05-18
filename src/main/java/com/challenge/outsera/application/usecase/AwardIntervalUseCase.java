package com.challenge.outsera.application.usecase;

import com.challenge.outsera.application.provider.MovieProvider;
import com.challenge.outsera.application.usecase.dto.AwardProducerResponse;
import com.challenge.outsera.application.usecase.dto.AwardResponse;
import com.challenge.outsera.application.usecase.dto.ProducerAwardIntervalInfo;
import com.challenge.outsera.domain.movie.MovieEntity;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AwardIntervalUseCase {
    private final MovieProvider movieProvider;

    public AwardResponse execute() {
        var winnerMovies = movieProvider.findWinnerMovies();

        Map<String, List<Integer>> producerWinYears = getProducerWinYears(winnerMovies);

        List<ProducerAwardIntervalInfo> infos = new ArrayList<>();

        for (var entry : producerWinYears.entrySet()) {
            String producerName = entry.getKey();
            List<Integer> years = entry.getValue().stream().distinct().sorted().toList();

            if (years.size() < 2) continue;

            ProducerAwardIntervalInfo info = getProducerAwardIntervalInfo(producerName, years);
            infos.add(info);
        }

        if (infos.isEmpty()) {
            return new AwardResponse(List.of(), List.of());
        }

        int globalMin = infos.stream().mapToInt(ProducerAwardIntervalInfo::getMinInterval).min().orElse(0);
        int globalMax = infos.stream().mapToInt(ProducerAwardIntervalInfo::getMaxInterval).max().orElse(0);

        List<AwardProducerResponse> minProducers = infos.stream()
            .filter(i -> i.getMinInterval() == globalMin)
            .map(i ->
                    new AwardProducerResponse(
                        i.getName(),
                        i.getMinInterval(),
                        i.getMinPrev(),
                        i.getMinNext()
                    )
            )
            .sorted(Comparator.comparing(AwardProducerResponse::producer))
            .toList();

        List<AwardProducerResponse> maxProducers = infos.stream()
            .filter(i -> i.getMaxInterval() == globalMax)
            .map(i ->
                new AwardProducerResponse(
                    i.getName(),
                    i.getMaxInterval(),
                    i.getMaxPrev(),
                    i.getMaxNext()
                )
            )
            .sorted(Comparator.comparing(AwardProducerResponse::producer))
            .toList();

        return new AwardResponse(minProducers, maxProducers);
    }

    private Map<String, List<Integer>> getProducerWinYears(List<MovieEntity> winnerMovies) {
        Map<String, List<Integer>> producerWinYears = new HashMap<>();

        for (var movie : winnerMovies) {
            for (var producer : movie.producers()) {
                producerWinYears
                    .computeIfAbsent(producer.name(), k -> new ArrayList<>())
                    .add(movie.year());
            }
        }
        return producerWinYears;
    }

    private ProducerAwardIntervalInfo getProducerAwardIntervalInfo(String producerName, List<Integer> years) {
        ProducerAwardIntervalInfo info = new ProducerAwardIntervalInfo(producerName);

        for (int i = 0; i < years.size() - 1; i++) {
            int prev = years.get(i);
            int next = years.get(i + 1);
            int interval = next - prev;

            if (interval < info.getMinInterval()) {
                info.setMinInterval(interval);
                info.setMinPrev(prev);
                info.setMinNext(next);
            }

            if (interval > info.getMaxInterval()) {
                info.setMaxInterval(interval);
                info.setMaxPrev(prev);
                info.setMaxNext(next);
            }
        }
        return info;
    }
}
