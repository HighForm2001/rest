package com.ocbc_rpp.rest.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Long transactionReference;
    private double amount;
    private String currency;
    private LocalDateTime transactionDate;
    private Long creatorID;
    private Long receiverID;

}

