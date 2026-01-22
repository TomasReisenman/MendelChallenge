package com.mendel.MendelChallenge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @PutMapping
    public ResponseEntity<Map<String,String>> getTransaction(){

        return ResponseEntity.accepted().build();
    }
}
