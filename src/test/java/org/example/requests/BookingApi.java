package org.example.requests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import org.example.payloads.Booking;

import static io.restassured.RestAssured.given;

public class BookingApi {

    private static final String apiUrl = "http://localhost:3000/booking/";

    public static Response postBooking(Booking payload) {
        return given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post(apiUrl);
    }

    public static Response deleteBooking(int bookingId, String authToken){
        return given()
                .header("cookie", "token=" + authToken)
                .contentType(ContentType.JSON)
                .when()
                .delete(apiUrl + Integer.toString(bookingId));
    }

    public static Response getBookings(String authToken) {
        return given()
                .header("cookie", "token=" + authToken)
                .contentType(ContentType.JSON)
                .when()
                .get(apiUrl);
    }

}
