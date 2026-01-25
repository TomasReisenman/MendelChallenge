package com.mendel.MendelChallenge.service;

import com.mendel.MendelChallenge.model.TransactionCreationRequest;

public class TransactionValidator {

  public static String validateTransaction(Long transactionId, TransactionCreationRequest request) {

    StringBuilder sb = new StringBuilder();

    if (request.getAmount() == null || request.getAmount().compareTo(0D) <= 0) {
      sb.append("Amount must be non null and a positive number");
    }

    if (request.getParentId() != null && request.getParentId().equals(transactionId)) {
      sb.append("Transaction Id can't be equal to parent id");

    }

    if (request.getType() == null || request.getType().isBlank()) {
      sb.append("Transaction must have a non empty type");
    }

    return sb.toString();
  }
}
