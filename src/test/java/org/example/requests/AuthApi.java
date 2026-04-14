package org.example.requests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.config.TestConfig;
import org.example.payloads.Auth;

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

    public static String getAuthToken (){
        // If the mock auth is enabled, no need to send a real token
        if (TestConfig.isMockingEnabled())
        {
            return "abc123";
        }
        // If mock auth is not up, then you hit the real Auth API
        Auth auth = new Auth("admin", "password");
        Response authResponse = postAuth(auth);
        if (authResponse.statusCode() != 200)
        {
            throw new RuntimeException("Failed to fetch auth token from real API, status got is:" + authResponse.getStatusCode());
        }

        return authResponse.getCookie("token");
    }
}
