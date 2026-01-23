package com.mendel.MendelChallenge.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {

    @LocalServerPort
    protected int port;

    @BeforeAll
    static void setupLogging() {
        enableLoggingOfRequestAndResponseIfValidationFails();

    }

    @BeforeEach
    void setupRestAssured() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }
}
