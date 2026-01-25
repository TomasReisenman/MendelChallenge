package com.mendel.MendelChallenge.model;

import lombok.Data;

@Data
public class TransactionCreationRequest {

  private Double amount;
  private String type;
  private Long parentId;

}
