package com.solvd.army;

import java.time.LocalDate;

public class ServiceTerm {

    LocalDate entered;
    LocalDate end;

    public ServiceTerm(LocalDate entered, LocalDate end) {
        this.entered = entered;
        this.end = end;
    }

    public ServiceTerm(LocalDate entered, int serviceTermInYears) {
        this.entered = entered;
        this.end = entered.plusYears(serviceTermInYears);
    }

    public LocalDate getEntered() {
        return entered;
    }

    public void setEntered(LocalDate entered) {
        this.entered = entered;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }
}
