package org.example.requests;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ReportApi {
    private static final String apiUrl = "http://localhost:8080/report/";

    public static Response getReport (String authToken){
        return given()
                .header("cookie", "token=" + authToken)
                .contentType(ContentType.JSON)
                .when()
                .get(apiUrl);
    }


}
