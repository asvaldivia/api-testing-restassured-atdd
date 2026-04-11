package org.example.stepdefinitions;

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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CancelBookingsStepDefs {

    Booking bookPayload;

    @Before("@cancel-bookings")
    public void setupAuthBookingData() {

        BookingDates date = new BookingDates(
                LocalDate.of(2025, 8, 9),
                LocalDate.of(2025, 8, 10)
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

    BookingResponse bookingResponse;
    @Given("There is a booking auth mock")
    public void there_is_a_booking() {

        Response bookingCreatedResponse = BookingApi.postBooking(bookPayload);
        assertEquals(201, bookingCreatedResponse.getStatusCode());

        bookingResponse = bookingCreatedResponse.as(BookingResponse.class);

    }
    Response deleteResponse;
    @When("Booking is cancelled auth mock")
    public void booking_is_cancelled() {
        // Calling a method that checks if infra is set for mocking or not in order to return a fixed token or
        // a real one hitting the real auth API.
        String authToken = AuthApi.getAuthToken();
        deleteResponse = BookingApi.deleteBooking(bookingResponse.getBookingid(), authToken);
    }

    @Then("Booking is cancelled successfully auth mock")
    public void booking_is_cancelled_successfully() {
        assertEquals(202, deleteResponse.getStatusCode());
    }

}
