package org.example.payloads;

import java.util.List;

public class AllBookingsResponse {
    private List<BookingRecord> bookings;

    public List<BookingRecord> getBookings() { return bookings; }
    public void setBookings(List<BookingRecord> bookings) { this.bookings = bookings; }

    public AllBookingsResponse() {}
}
