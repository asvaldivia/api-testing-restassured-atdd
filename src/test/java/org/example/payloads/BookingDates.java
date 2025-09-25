package org.example.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class BookingDates {

    @JsonProperty
    private LocalDate checkin;
    @JsonProperty
    private LocalDate checkout;

    public BookingDates(LocalDate checkin, LocalDate checkout){
        this.checkin = checkin;
        this.checkout = checkout;
    }
}
