package de.ahus1.lottery.domain;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Draw {

    private final LocalDate date;
    private Set<Integer> numbers;

    private Draw(LocalDate aDate, Set<Integer> someNumbers) {
        numbers = someNumbers;
        date = aDate;
    }

    public Set<Integer> getNumbers() {
        return numbers;
    }

    public LocalDate getDate() {
        return date;
    }

    public static class DrawBuilder {
        public static final int DRAW_SIZE = 6;
        Set<Integer> numbers = new HashSet<Integer>();
        private LocalDate date;

        public DrawBuilder withNumber(Integer aNumber) {
            if (numbers.contains(aNumber)) {
                throw new IllegalArgumentException("you must not add the same number twice ("
                        + aNumber + ")");
            }
            numbers.add(aNumber);
            return this;
        }

        public boolean contains(Integer aNumber) {
            return numbers.contains(aNumber);
        }

        public int size() {
            return numbers.size();
        }

        public DrawBuilder withNumbers(Integer... someNumbers) {
            List<Integer> list = Arrays.asList(someNumbers);
            list.forEach(n -> withNumber(n));
            return this;
        }

        public Draw build() {
            if (numbers.size() != DRAW_SIZE) {
                throw new IllegalStateException("you need " + DRAW_SIZE + " numbers in a draw!");
            }
            return new Draw(date, numbers);
        }

        public boolean isComplete() {
            return numbers.size() == DRAW_SIZE;
        }

        public DrawBuilder withDate(LocalDate aDate) {
            date = aDate;
            return this;
        }
    }

    public static DrawBuilder builder() {
        return new DrawBuilder();
    }

}
