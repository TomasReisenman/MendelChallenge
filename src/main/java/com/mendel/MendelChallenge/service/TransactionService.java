package com.mendel.MendelChallenge.service;

import com.mendel.MendelChallenge.exception.TransactionAlreadyExistsException;
import com.mendel.MendelChallenge.exception.TransactionNotFoundException;
import com.mendel.MendelChallenge.model.Transaction;
import com.mendel.MendelChallenge.model.TransactionCreationRequest;
import com.mendel.MendelChallenge.model.TransactionCreationResult;
import com.mendel.MendelChallenge.model.TransactionSumResponse;
import com.mendel.MendelChallenge.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransactionService {

  private TransactionRepository transactionRepository;

  public TransactionService(TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  public TransactionCreationResult processTransaction(
      Long transactionId, TransactionCreationRequest request) {
    Transaction transaction = TransactionMapper.mapToEntity(transactionId, request);

    TransactionCreationResult result = new TransactionCreationResult("ok");
    if (transaction.getParentId() == null) {

      Transaction savedTransaction = this.transactionRepository.getTransactionStorage()
          .putIfAbsent(transaction.getTransactionId(), transaction);

      if (savedTransaction != null) {
        throw new TransactionAlreadyExistsException(
            "Transaction id : " + transaction.getTransactionId() + " already exists");
      }

      this.transactionRepository.storeTransactionInTypeStorage(transaction.getType(),
          transaction.getTransactionId());

      return result;
    }

    Transaction parentTransaction = this.transactionRepository
        .findTransaction(transaction.getParentId());
    if (parentTransaction == null) {
      throw new TransactionNotFoundException("Parent Transaction id : " +
          transaction.getParentId() + " not found");
    }

    Transaction savedTransaction = this.transactionRepository.getTransactionStorage()
        .putIfAbsent(transaction.getTransactionId(), transaction);

    if (savedTransaction != null) {
      throw new TransactionAlreadyExistsException(
          "Transaction id : " + transaction.getTransactionId() + " already exists");
    }

    this.transactionRepository.storeTransactionInTypeStorage(transaction.getType(),
        transaction.getTransactionId());

    this.transactionRepository.storeTransactionInRelationStorage(transaction.getParentId(),
        transaction.getTransactionId());


    return result;
  }

  public Set<Long> findAllTransactionsIdsByType(String type) {
    Set<Long> allTransactionsIdsByType = this.transactionRepository
        .findAllTransactionsIdsByType(type);

    return allTransactionsIdsByType != null ? allTransactionsIdsByType : Collections.emptySet();
  }

  public TransactionSumResponse getTransactionSum(Long transactionId) {

    Transaction foundTransaction = this.transactionRepository.findTransaction(transactionId);
    if (foundTransaction == null) {
      throw new TransactionNotFoundException("Transaction id : " + transactionId + " not found");
    }

    Double currentAmount = 0D;
    TransactionSumResponse sumResponse = new TransactionSumResponse();

    Map<Long, Set<Long>> relations = this.transactionRepository.getTransactionRelations();

    Queue<Long> queue = new ArrayDeque<>();
    queue.add(transactionId);

    while (!queue.isEmpty()) {

      Long current = queue.poll();
      currentAmount += this.transactionRepository.findTransaction(current).getAmount();

      Set<Long> children = relations.get(current);
      if (children != null && !children.isEmpty()) {
        queue.addAll(children);
      }

    }
    sumResponse.setSum(currentAmount);
    return sumResponse;
  }
}
