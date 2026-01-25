package com.mendel.MendelChallenge.exception;

public class TransactionNotFoundException extends RuntimeException {

  public TransactionNotFoundException(String message) {
    super(message);
  }
}
