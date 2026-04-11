package org.example.stepdefinitions;

import infrastructure.process.PortWaiter;
import infrastructure.process.ProcessManager;
import infrastructure.wiremock.WireMockManager;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.example.config.TestConfig;
import org.example.payloads.*;
import org.example.requests.AuthApi;
import org.example.requests.BookingApi;
import io.cucumber.java.Before;
import org.junit.jupiter.api.Test;

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
        String authToken = "";
        if(!TestConfig.isMockingEnabled()) {
            Auth auth = new Auth(
                    "admin",
                    "password"
            );
            Response authResponse = AuthApi.postAuth(auth);
            authToken = authResponse.getCookie("token");
            deleteResponse = BookingApi.deleteBooking(bookingResponse.getBookingid(), authToken);

        } else {
            deleteResponse = BookingApi.deleteBooking(bookingResponse.getBookingid(), "asd123");
        }


    }

    @Then("Booking is cancelled successfully auth mock")
    public void booking_is_cancelled_successfully() {
        assertEquals(202, deleteResponse.getStatusCode());
    }

}
