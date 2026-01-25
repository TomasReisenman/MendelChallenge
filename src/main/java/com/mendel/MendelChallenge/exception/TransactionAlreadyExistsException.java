package com.mendel.MendelChallenge.exception;

public class TransactionAlreadyExistsException extends RuntimeException {

  public TransactionAlreadyExistsException(String message) {
    super(message);
  }
}
