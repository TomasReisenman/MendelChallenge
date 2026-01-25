package com.mendel.MendelChallenge.integration;

import com.mendel.MendelChallenge.model.TransactionCreationRequest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

public class TransactionTest extends BaseIntegrationTest {

  private static final String TRANSACTION_URL = "/transactions/";

  @Test
  void verify_created_transactions_are_returned_in_type_query() {

    TransactionCreationRequest testTransaction = new TransactionCreationRequest();
    testTransaction.setAmount(500D);
    testTransaction.setType("car");
    testTransaction.setParentId(null);

    given()
        .when()
        .body(testTransaction)
        .put(TRANSACTION_URL + 1)
        .then()
        .log().body()
        .statusCode(HttpStatus.SC_CREATED)
        .body("status", equalTo("ok"));

    given()
        .when()
        .body(testTransaction)
        .put(TRANSACTION_URL + 2)
        .then()
        .log().body()
        .statusCode(HttpStatus.SC_CREATED)
        .body("status", equalTo("ok"));

    given()
        .when()
        .body(testTransaction)
        .get(TRANSACTION_URL + "types/car")
        .then()
        .log().body()
        .statusCode(HttpStatus.SC_OK)
        .body("$", containsInAnyOrder(1, 2));
  }

  @Test
  void verify_parent_sum_is_correct() {

    TransactionCreationRequest transaction1 = new TransactionCreationRequest();
    transaction1.setAmount(500D);
    transaction1.setType("plane");
    transaction1.setParentId(null);

    TransactionCreationRequest transaction2 = new TransactionCreationRequest();
    transaction2.setAmount(700D);
    transaction2.setType("plane");
    transaction2.setParentId(5L);

    TransactionCreationRequest transaction3 = new TransactionCreationRequest();
    transaction3.setAmount(800D);
    transaction3.setType("plane");
    transaction3.setParentId(5L);

    TransactionCreationRequest transaction4 = new TransactionCreationRequest();
    transaction4.setAmount(900D);
    transaction4.setType("plane");
    transaction4.setParentId(7L);

    List<TransactionCreationRequest> transactions = List.of(transaction1
        , transaction2, transaction3, transaction4);

    Long transactionId = 5L;
    for (TransactionCreationRequest request : transactions) {

      given()
          .when()
          .body(request)
          .put(TRANSACTION_URL + transactionId);

      transactionId++;
    }

    given()
        .when()
        .get(TRANSACTION_URL + "sum/5")
        .then()
        .log().body()
        .statusCode(HttpStatus.SC_OK)
        .body("sum", equalTo(2900.0F));

  }
}
