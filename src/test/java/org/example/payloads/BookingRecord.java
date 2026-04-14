package org.example.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookingRecord {

    @JsonProperty("bookingid")
    private int bookingid;
    @JsonProperty("roomid")
    private int roomid;
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("lastname")
    private String lastname;
    @JsonProperty("depositpaid")
    private boolean depositpaid;
    @JsonProperty("bookingdates")
    private BookingDates bookingdates;

    public BookingRecord() {}


    public int getBookingid() { return bookingid; }
    public void setBookingid(int bookingid) { this.bookingid = bookingid; }
    public int getRoomid() { return roomid; }
    public void setRoomid(int roomid) { this.roomid = roomid; }
    public BookingDates getBookingdates() { return bookingdates; }
    public void setBookingdates(BookingDates bookingdates) { this.bookingdates = bookingdates; }
}
