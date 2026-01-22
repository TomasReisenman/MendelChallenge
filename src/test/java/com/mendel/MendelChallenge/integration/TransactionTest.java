package com.mendel.MendelChallenge.integration;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TransactionTest extends BaseIntegrationTest {

    @Test
    void verify_created_transactions_are_accepted() {
        given()
                .when()
                .put("/transactions")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_ACCEPTED);
    }
}
