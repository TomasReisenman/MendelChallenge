package com.mendel.MendelChallenge.exception;

public class TransactionValidationException extends RuntimeException {
  public TransactionValidationException(String message) {
    super(message);
  }
}
