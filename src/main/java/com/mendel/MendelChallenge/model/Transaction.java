package com.mendel.MendelChallenge.model;

import lombok.Data;

@Data
public class Transaction {

  private Long transactionId;
  private Double amount;
  private String type;
  private Long parentId;
}
