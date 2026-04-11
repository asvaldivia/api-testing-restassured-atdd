package org.example.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.example.config.TestConfig;
import org.example.payloads.*;
import org.example.requests.AuthApi;
import org.example.requests.BookingApi;
import io.cucumber.java.Before;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BookingsStepDefs {

    String authToken;
    Booking bookPayload;


    @Before("@bookings")
    public void setupAuthBookingData() {
        BookingDates date = new BookingDates(
                LocalDate.of(2026, 8, 9),
                LocalDate.of(2026, 8, 10)
        );

        this.bookPayload = new Booking(
                1,
                "Alvaro",
                "Val",
                200,
                true,
                date,
                "Breakfast"
        );
    }

    @Given("Rooms and dates available")
    public void rooms_and_dates_available() {
        Auth auth = new Auth(
                "admin",
                "password"
        );

        Response authResponse = AuthApi.postAuth(auth);
        this.authToken = authResponse.getCookie("token");

        Response bookings = BookingApi.getBookings(this.authToken);
        assertEquals(200, bookings.getStatusCode(), "Status code is 200");

        // cast the response body to an entity
        AllBookingsResponse allBookings = bookings.as(AllBookingsResponse.class);
        List<BookingRecord> existingBookings = allBookings.getBookings();
        // assert that the room and date is available
        int desiredRoomId = this.bookPayload.getRoomid();
        BookingDates desiredDates = this.bookPayload.getDates();

        boolean conflict = existingBookings.stream()
                .anyMatch(record ->
                        record.getRoomid() == desiredRoomId &&
                        isDateConflict(record.getBookingdates(), desiredDates)
                        );

        // Assert: If conflictFound is TRUE, the assertion FAILS.
        assertFalse(conflict,
                String.format("Conflict found for Room ID %d between %s and %s.",
                        desiredRoomId,
                        desiredDates.getCheckin(),
                        desiredDates.getCheckout()));
    }

    /**
     * Helper method to determine if two date ranges overlap.
     * Overlap occurs if (StartA < EndB) AND (EndA > StartB).
     */
    private boolean isDateConflict(BookingDates existing, BookingDates desired) {
        LocalDate existingCheckIn = existing.getCheckin();
        LocalDate existingCheckOut = existing.getCheckout();
        LocalDate desiredCheckIn = desired.getCheckin();
        LocalDate desiredCheckOut = desired.getCheckout();

        // Check for overlap, allowing for same-day checkin/checkout logic often used in APIs:
        return (desiredCheckIn.isBefore(existingCheckOut) || desiredCheckIn.isEqual(existingCheckOut)) &&
                (desiredCheckOut.isAfter(existingCheckIn) || desiredCheckOut.isEqual(existingCheckIn));
    }
    Response bookingCreatedResponse;
    @When("User books a room")
    public void user_books_a_room() {
        this.bookingCreatedResponse = BookingApi.postBooking(bookPayload);
    }

    @Then("Room is booked successfully")
    public void room_is_booked_successfully() {
        assertEquals(201, bookingCreatedResponse.getStatusCode());
    }

    @And("Room and date is not available to be booked")
    public void room_and_date_is_not_available_to_be_booked() {

        // Post the same book again
        Response conflictResponse = BookingApi.postBooking(bookPayload);
        // Assert error status - already booked
        assertEquals(409, conflictResponse.getStatusCode());
    }
    BookingResponse bookingResponse;
    @Given("There is a booking")
    public void there_is_a_booking() {

        Response bookingCreatedResponse = BookingApi.postBooking(bookPayload);
        assertEquals(201, bookingCreatedResponse.getStatusCode());

        bookingResponse = bookingCreatedResponse.as(BookingResponse.class);

    }
    Response deleteResponse;
    @When("Booking is cancelled")
    public void booking_is_cancelled() {
        if (!TestConfig.isMockingEnabled()) {
            Auth auth = new Auth(
                    "admin",
                    "password"
            );

            Response authResponse = AuthApi.postAuth(auth);
            this.authToken = authResponse.getCookie("token");
        }

        deleteResponse = BookingApi.deleteBooking(bookingResponse.getBookingid(), this.authToken);
    }


    @Then("Booking is cancelled successfully")
    public void booking_is_cancelled_successfully() {
        assertEquals(202, deleteResponse.getStatusCode());
    }
    @And("Room and date is available again to be booked")
    public void room_and_date_is_available_again_to_be_booked() {
        Response bookings = BookingApi.getBookings(this.authToken);
        assertEquals(200, bookings.getStatusCode(), "Status code is 200");

        // cast the response body to an entity
        AllBookingsResponse allBookings = bookings.as(AllBookingsResponse.class);
        List<BookingRecord> existingBookings = allBookings.getBookings();
        // assert that the room and date is available
        int desiredRoomId = this.bookPayload.getRoomid();
        BookingDates desiredDates = this.bookPayload.getDates();

        boolean conflict = existingBookings.stream()
                .anyMatch(record ->
                        record.getRoomid() == desiredRoomId &&
                                isDateConflict(record.getBookingdates(), desiredDates)
                );

        // Assert: If conflictFound is TRUE, the assertion FAILS.
        assertFalse(conflict,
                String.format("Conflict found for Room ID %d between %s and %s.",
                        desiredRoomId,
                        desiredDates.getCheckin(),
                        desiredDates.getCheckout()));
    }

    @After("@bookings")
    public void delete_bookings_teardown() {
        if (bookingCreatedResponse != null) {
            BookingResponse bookingResponse =
                    bookingCreatedResponse.as(BookingResponse.class);

            Response deleteResponse1 =
                    BookingApi.deleteBooking(
                            bookingResponse.getBookingid(),
                            this.authToken
                    );
            assertEquals(202, deleteResponse1.getStatusCode());
        }
    }
}
