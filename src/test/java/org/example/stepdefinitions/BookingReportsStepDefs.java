package org.example.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.example.payloads.*;
import org.example.requests.AuthApi;
import org.example.requests.BookingApi;
import org.example.requests.ReportApi;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class BookingReportsStepDefs {
    BookingResponse response1;
    BookingResponse response2;
    @Given("I have multiple bookings")
    public void i_have_multiple_bookings() {

        BookingDates dates1 = new BookingDates(
                LocalDate.of(2025,11, 9),
                LocalDate.of(2025,11, 13)
        );

        BookingDates dates2 = new BookingDates(
                LocalDate.of(2025,12, 9),
                LocalDate.of(2025,12, 13)
        );

        Booking payloadOne = new Booking(
                1,
                "Mark",
                "Winteringham",
                200,
                true,
                dates1,
                "Breakfast"
        );


        Booking payloadTwo = new Booking(
                1,
                "Alvaro",
                "Val",
                200,
                true,
                dates2,
                "Breakfast"
        );
        // Deserialize here, get the bookingId
        Response booking1 = BookingApi.postBooking(payloadOne);
        Response booking2 = BookingApi.postBooking(payloadTwo);

        this.response1 = booking1.as(BookingResponse.class);
        this.response2 = booking2.as(BookingResponse.class);

    }
    private Response totalReportResponse;
    @When("I ask for a report of my totals of bookings")
    public void i_ask_for_a_report_on_my_total_earnings() {
        Auth auth = new Auth(
                "admin",
                "password"
        );
        Response authRespose = AuthApi.postAuth(auth);

        this.totalReportResponse = ReportApi.getReport(authRespose.getCookie("token"));

        assertEquals(200, totalReportResponse.getStatusCode(), "Status code is 200");

    }
    ReportResponse reportResponse;
    @Then("I will receive a list of bookings")
    public void i_will_receive_a_list_of_bookings() {
        // Get total earnings, and assert
        this.reportResponse = totalReportResponse.as(ReportResponse.class);
        assertNotNull(this.reportResponse.getReport(), "Got a non empty response");
        assertTrue(this.reportResponse.getReport() instanceof List, "The report must be a list of objects");
    }
    @And("Each booking is represented by an object")
    public void each_booking_is_represented_by_an_object() {
        this.totalReportResponse.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("jsonschemas/reportSchema.json"));

    }
    @And("Total of bookings are correct")
    public void total_of_bookings_are_correct() {
        // Get total earnings, and assert
        assertTrue(reportResponse.getReport().size() >= 2, "Bookings reported is equal or greater than to 2");
    }

    // Teardown is needed, specifically for delete bookings to make
    // this idempotent
    @After
    public void delete_bookings_teardown() {
        Auth auth = new Auth(
                "admin",
                "password"
        );

        Response authRespose = AuthApi.postAuth(auth);

        if (response1 != null) {
           Response deleteResponse1 = BookingApi.deleteBooking(response1.getBookingid(), authRespose.getCookie("token"));
           assertEquals(202, deleteResponse1.getStatusCode());
        }
        if (response2 != null){
            Response deleteResponse2 = BookingApi.deleteBooking(response2.getBookingid(), authRespose.getCookie("token"));
            assertEquals(202, deleteResponse2.getStatusCode());
        }
    }
}
