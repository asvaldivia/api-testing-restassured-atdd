package org.example.requests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.payloads.Auth;
import org.example.payloads.Booking;

import static io.restassured.RestAssured.given;

public class AuthApi {
    private static final String apiUrl = "http://127.0.0.1:8080/auth/login";

    public static Response postAuth(Auth payload) {
        return given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post(apiUrl);
    }
}
