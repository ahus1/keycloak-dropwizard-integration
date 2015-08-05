package domain;

import de.ahus1.lottery.domain.Draw;
import de.ahus1.lottery.domain.DrawingService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.fail;

public class DrawingServiceTest {

    @Test
    public void shouldDrawSixNumbers() {
        LocalDate date = LocalDate.parse("2012-01-01");
        Draw draw = DrawingService.drawNumbers(date);
        Assertions.assertThat(draw.getNumbers()).containsExactly(16, 33, 21, 23, 9, 13);
    }

    @Test
    public void shouldGiveDifferentNumbersOnDifferentDates() {
        LocalDate date = LocalDate.parse("2012-01-03");
        Draw draw = DrawingService.drawNumbers(date);
        Assertions.assertThat(draw.getNumbers()).containsExactly(34, 37, 27, 45, 15, 31);
    }

    @Test
    public void shouldDrawOneAsMinimum() {
        LocalDate date = LocalDate.parse("2012-01-01");
        LocalDate maxDate = LocalDate.parse("2013-01-01");
        while(true) {
            Draw draw = DrawingService.drawNumbers(date);
            if(draw.getNumbers().contains(1)) {
                break;
            }
            date = date.plusDays(1);
            if(date.isAfter(maxDate)) {
                fail("didn't find 1 in the draw");
            }
        }
    }

    @Test
    public void shouldDrawFourtyNineAsMaximum() {
        LocalDate date = LocalDate.parse("2012-01-01");
        LocalDate maxDate = LocalDate.parse("2013-01-01");
        while(true) {
            Draw draw = DrawingService.drawNumbers(date);
            if(draw.getNumbers().contains(49)) {
                break;
            }
            date = date.plusDays(1);
            if(date.isAfter(maxDate)) {
                fail("didn't find 49 in the draw");
            }
        }
    }

}

