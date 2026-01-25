package com.mendel.MendelChallenge.service;

import com.mendel.MendelChallenge.model.Transaction;
import com.mendel.MendelChallenge.model.TransactionCreationRequest;

public class TransactionMapper {
  public static Transaction mapToEntity(Long transactionId, TransactionCreationRequest original) {
    Transaction transaction = new Transaction();
    transaction.setTransactionId(transactionId);
    transaction.setType(original.getType());
    transaction.setParentId(original.getParentId());
    transaction.setAmount(original.getAmount());

    return transaction;
  }
}
