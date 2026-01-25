package com.mendel.MendelChallenge.integration;

import com.mendel.MendelChallenge.model.TransactionCreationRequest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TransactionTest extends BaseIntegrationTest {

  private static final String TRANSACTION_URL = "/transactions/";

  @Test
  void verify_created_transactions_are_returned_in_type_query() {

    TransactionCreationRequest testTransaction = new TransactionCreationRequest();
    testTransaction.setAmount(500D);
    testTransaction.setType("trip");
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
        .get(TRANSACTION_URL + "types/trip")
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

  @Test
  void verify_both_sum_and_type_query_return_correct_result() {

    TransactionCreationRequest req1 = new TransactionCreationRequest();
    req1.setAmount(5000D);
    req1.setType("cars");
    req1.setParentId(null);

    TransactionCreationRequest req2 = new TransactionCreationRequest();
    req2.setAmount(10000D);
    req2.setType("shopping");
    req2.setParentId(10L);

    TransactionCreationRequest req3 = new TransactionCreationRequest();
    req3.setAmount(5000D);
    req3.setType("shopping");
    req3.setParentId(11L);

    List<TransactionCreationRequest> reqs = List.of(req1
        , req2, req3);

    Long transactionId = 10L;
    for (TransactionCreationRequest request : reqs) {

      given()
          .when()
          .body(request)
          .put(TRANSACTION_URL + transactionId);

      transactionId++;
    }


    given()
        .when()
        .get(TRANSACTION_URL + "types/cars")
        .then()
        .log().body()
        .statusCode(HttpStatus.SC_OK)
        .body("$", containsInAnyOrder(10));

    given()
        .when()
        .get(TRANSACTION_URL + "sum/10")
        .then()
        .log().body()
        .statusCode(HttpStatus.SC_OK)
        .body("sum", equalTo(20000.0F));


    given()
        .when()
        .get(TRANSACTION_URL + "sum/11")
        .then()
        .log().body()
        .statusCode(HttpStatus.SC_OK)
        .body("sum", equalTo(15000.0F));

  }

  @Test
  void verify_transaction_with_zero_amount_is_rejected() {
    TransactionCreationRequest request = new TransactionCreationRequest();
    request.setAmount(0D);
    request.setType("cars");
    request.setParentId(null);

    given()
        .when()
        .body(request)
        .put(TRANSACTION_URL + 13)
        .then()
        .log().body()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  void verify_transaction_with_empty_type_is_rejected() {
    TransactionCreationRequest request = new TransactionCreationRequest();
    request.setAmount(300D);
    request.setType("");
    request.setParentId(null);

    given()
        .when()
        .body(request)
        .put(TRANSACTION_URL + 14)
        .then()
        .log().body()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  void verify_transaction_with_id_equal_to_parent_id_is_rejected() {
    TransactionCreationRequest request = new TransactionCreationRequest();
    request.setAmount(300D);
    request.setType("trip");
    request.setParentId(15L);

    given()
        .when()
        .body(request)
        .put(TRANSACTION_URL + 15)
        .then()
        .log().body()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  void verify_transaction_with_non_existing_parent_id_is_rejected() {
    TransactionCreationRequest request = new TransactionCreationRequest();
    request.setAmount(300D);
    request.setType("trip");
    request.setParentId(25L);

    given()
        .when()
        .body(request)
        .put(TRANSACTION_URL + 16)
        .then()
        .log().body()
        .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  void verify_sum_request_of_non_existing_transaction_is_rejected() {

    given()
        .when()
        .get(TRANSACTION_URL + "sum/25")
        .then()
        .log().body()
        .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  void verify_type_request_with_no_transactions_of_that_type_return_empty_list() {

    given()
        .when()
        .get(TRANSACTION_URL + "types/restaurant")
        .then()
        .log().body()
        .statusCode(HttpStatus.SC_OK)
        .body("$", empty());
  }

  @Test
  void verify_transaction_with_already_existing_id_is_rejected() {
    TransactionCreationRequest request = new TransactionCreationRequest();
    request.setAmount(300D);
    request.setType("trip");
    request.setParentId(null);

    given()
        .when()
        .body(request)
        .put(TRANSACTION_URL + 17)
        .then()
        .log().body()
        .statusCode(HttpStatus.SC_CREATED)
        .body("status", equalTo("ok"));

    given()
        .when()
        .body(request)
        .put(TRANSACTION_URL + 17)
        .then()
        .log().body()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }
}
