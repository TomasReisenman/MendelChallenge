package com.mendel.MendelChallenge.integration;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {

  @LocalServerPort
  protected int port;

  @BeforeAll
  static void setup() {

    JsonMapper mapper = JsonMapper.builder()
        .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        .build();

    RestAssured.config = RestAssuredConfig.config()
        .logConfig(LogConfig.logConfig()
            .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL))
        .objectMapperConfig(new ObjectMapperConfig()
            .jackson3ObjectMapperFactory((cls, charset) -> mapper)
        );

  }

  @BeforeEach
  void setupRestAssured() {
    RestAssured.port = port;
    RestAssured.baseURI = "http://localhost";
    RestAssured.requestSpecification = new RequestSpecBuilder()
        .addHeader("Content-Type", "application/json")
        .build();
  }
}
