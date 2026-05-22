package com.challenge.outsera.application.usecase;

import com.challenge.outsera.application.provider.MovieProvider;
import com.challenge.outsera.application.usecase.awardinterval.GlobalResult;
import com.challenge.outsera.application.usecase.awardinterval.IntervalBounds;
import com.challenge.outsera.application.usecase.dto.AwardResponse;
import com.challenge.outsera.application.usecase.dto.ProducerWinYear;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AwardIntervalUseCase {
    private final MovieProvider movieProvider;

    public AwardResponse execute() {
        List<ProducerWinYear> rows = movieProvider.findProducerWinYearsByWinnerMovies();

        if (rows.isEmpty()) {
            return GlobalResult.empty();
        }

        GlobalResult result = new GlobalResult();
        String currentProducer = rows.getFirst().producerName();
        IntervalBounds bounds = new IntervalBounds();
        Integer previousYear = null;
        int winCount = 0;

        for (ProducerWinYear row : rows) {
            if (!row.producerName().equals(currentProducer)) {
                result.registerProducer(currentProducer, winCount, bounds);
                currentProducer = row.producerName();
                bounds = new IntervalBounds();
                previousYear = null;
                winCount = 0;
            }

            winCount++;
            if (previousYear != null) {
                int interval = row.year() - previousYear;

                bounds.register(interval, previousYear, row.year());
            }
            previousYear = row.year();
        }

        result.registerProducer(currentProducer, winCount, bounds);

        if (result.isEmpty()) {
            return GlobalResult.empty();
        }

        return result.toAwardResponse();
    }
}
