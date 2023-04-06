package com.ocbc_rpp.rest.models.request;


import lombok.Data;

@Data
public class TransactionCreateRequest {
    private double amount;
    private String currency;
    private long from_acc;
    private long to_acc;
}


