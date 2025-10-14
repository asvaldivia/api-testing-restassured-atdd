package org.example.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.example.payloads.Auth;
import org.example.payloads.Booking;
import org.example.payloads.BookingDates;
import org.example.requests.AuthApi;
import org.example.requests.BookingApi;
import org.junit.Before;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingsStepDefs {

    Response authResponse;
    Booking bookPayload;
    // Setup
    @Before
    public void setup_auth_booking_data() {
        Auth auth = new Auth(
                "admin",
                "password"
        );

        this.authResponse = AuthApi.postAuth(auth);

        BookingDates date = new BookingDates(
                LocalDate.of(2025, 12, 9),
                LocalDate.of(2025, 12, 10)
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
        Response bookings = BookingApi.getBookings(this.authResponse.getCookie("token"));

    }

    @When("User books a room")
    public void user_books_a_room() {

    }

    @Then("Room is booked successfully")
    public void room_is_booked_successfully() {

    }

    @And("Room and date is not available to be booked")
    public void room_and_date_is_not_available_to_be_booked() {

    }
    @Given("There is a booking")
    public void there_is_a_booking() {

    }

    @When("Booking is cancelled")
    public void booking_is_cancelled() {

    }

    @Then("Booking is cancelled successfully")
    public void booking_is_cancelled_successfully() {

    }
    @And("Room and date is available again to be booked")
    public void room_and_date_is_available_again_to_be_booked() {

    }

}
