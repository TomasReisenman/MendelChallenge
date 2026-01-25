package com.mendel.MendelChallenge.repository;

import com.mendel.MendelChallenge.model.Transaction;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransactionRepository {

  private Map<Long, Transaction> transactionStorage;
  private Map<String, Set<Long>> transactionTypeStorage;
  private Map<Long, Set<Long>> transactionRelationStorage;

  @PostConstruct
  public void setUpInMemoryStorage() {
    this.transactionStorage = new ConcurrentHashMap<>();
    this.transactionTypeStorage = new ConcurrentHashMap<>();
    this.transactionRelationStorage = new ConcurrentHashMap<>();

  }

  public Set<Long> findAllTransactionsIdsByType(String type) {
    return this.transactionTypeStorage.get(type);
  }

  public Map<Long, Set<Long>> getTransactionRelations() {
    return this.transactionRelationStorage;
  }

  public Transaction findTransaction(Long transactionId) {
    return this.transactionStorage.get(transactionId);
  }

  public Map<Long, Transaction> getTransactionStorage() {
    return this.transactionStorage;
  }

  public void storeTransactionInTypeStorage(String type, Long transactionId) {
    this.transactionTypeStorage.computeIfAbsent(type, t ->
        ConcurrentHashMap.newKeySet()).add(transactionId);
  }

  public void storeTransactionInRelationStorage(Long parentId, Long transactionId) {
    this.transactionRelationStorage.computeIfAbsent(parentId, t ->
        ConcurrentHashMap.newKeySet()).add(transactionId);
  }
}
