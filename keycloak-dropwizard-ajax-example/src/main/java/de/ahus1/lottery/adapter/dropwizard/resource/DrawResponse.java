package de.ahus1.lottery.adapter.dropwizard.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.ahus1.lottery.domain.Draw;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class DrawResponse {
    private Set<Integer> numbers;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    public DrawResponse() {
    }

    public DrawResponse(Draw draw) {
        this.date = draw.getDate();
        this.numbers = new HashSet<Integer>(draw.getNumbers());
    }

    public Set<Integer> getNumbers() {
        return numbers;
    }

    public void setNumbers(Set<Integer> numbers) {
        this.numbers = numbers;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
