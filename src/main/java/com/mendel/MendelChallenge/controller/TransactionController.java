package com.mendel.MendelChallenge.controller;

import com.mendel.MendelChallenge.model.TransactionCreationRequest;
import com.mendel.MendelChallenge.model.TransactionCreationResult;
import com.mendel.MendelChallenge.model.TransactionSumResponse;
import com.mendel.MendelChallenge.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

  private TransactionService transactionService;

  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;

  }

  @PutMapping("/{transaction_id}")
  public ResponseEntity<TransactionCreationResult> storeTransaction(
      @PathVariable("transaction_id") Long transactionId, @RequestBody TransactionCreationRequest transaction) {

    TransactionCreationResult result = this.transactionService.processTransaction(transactionId, transaction);

    if (result.getStatus().equals("ok")) {
      return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(result);
  }

  @GetMapping("/types/{type}")
  public ResponseEntity<Set<Long>> getTransactionsByType(
      @PathVariable("type") String type) {

    Set<Long> transactionIds = this.transactionService.findAllTransactionsIdsByType(type);

    return ResponseEntity.status(HttpStatus.OK).body(transactionIds);
  }


  @GetMapping("/sum/{transaction_id}")
  public ResponseEntity<TransactionSumResponse> getTransactionSum(
      @PathVariable("transaction_id") Long transactionId) {

    TransactionSumResponse transactionSum = this.transactionService.getTransactionSum(transactionId);

    return ResponseEntity.status(HttpStatus.OK).body(transactionSum);
  }
}
