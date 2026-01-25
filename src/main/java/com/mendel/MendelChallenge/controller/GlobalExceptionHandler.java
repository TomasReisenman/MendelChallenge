package com.mendel.MendelChallenge.controller;

import com.mendel.MendelChallenge.exception.TransactionAlreadyExistsException;
import com.mendel.MendelChallenge.exception.TransactionNotFoundException;
import com.mendel.MendelChallenge.exception.TransactionValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private record ErrorMessage(String message) {
  }

  @ExceptionHandler(TransactionNotFoundException.class)
  public ResponseEntity<ErrorMessage> handleTransactionNotFoundException(
      TransactionNotFoundException ex) {
    return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({TransactionAlreadyExistsException.class, TransactionValidationException.class})
  public ResponseEntity<ErrorMessage> handleUnprocessableTransactionException(
      RuntimeException ex) {
    return new ResponseEntity<>(new ErrorMessage(ex.getMessage()),
        HttpStatus.UNPROCESSABLE_CONTENT);
  }

}
