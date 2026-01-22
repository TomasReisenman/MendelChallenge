package com.mendel.MendelChallenge.integration;

import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;

public abstract class BaseIntegrationTest {

    @BeforeAll
    static void setupLogging() {
        enableLoggingOfRequestAndResponseIfValidationFails();

        RestAssured.config = RestAssuredConfig.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
    }

    @BeforeEach
    void setupRestAssured() {
        RestAssured.port = 8080;
        RestAssured.baseURI = "http://localhost";

    }
}
