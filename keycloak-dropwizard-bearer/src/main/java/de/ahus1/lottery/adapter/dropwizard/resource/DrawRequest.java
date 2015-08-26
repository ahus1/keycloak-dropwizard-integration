package de.ahus1.lottery.adapter.dropwizard.resource;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class DrawRequest {

    @NotNull
    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
