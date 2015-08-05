package de.ahus1.lottery.domain;

import java.time.LocalDate;
import java.util.Random;

public class DrawingService {

    public static final int MAX_NUMBER_IN_DRAW = 49;
    public static final int MIN_NUMBER_IN_DRAW = 1;

    public static Draw drawNumbers(LocalDate date) {
        Random random = new Random(date.toEpochDay());
        Draw.DrawBuilder builder = Draw.builder().withDate(date);
        do {
            Integer number = random.nextInt(MAX_NUMBER_IN_DRAW + 1);
            if(number < MIN_NUMBER_IN_DRAW) {
                continue;
            }
            if(builder.contains(number)) {
                continue;
            }
            builder.withNumber(number);
        } while (!builder.isComplete());
        return builder.build();
    }
}
