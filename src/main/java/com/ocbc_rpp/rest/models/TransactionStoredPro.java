package com.ocbc_rpp.rest.models;

import com.ocbc_rpp.rest.models.dto.TransactionDto;

import java.sql.Timestamp;

public class TransactionStoredPro {
    private Long transaction_reference;
    private double amount;
    private Long from_acc_id;
    private Timestamp transaction_date;

    public TransactionDto toDto(){
        TransactionDto dto = new TransactionDto();
        dto.setTransactionReference(this.transaction_reference);
        dto.setTransactionDate(this.transaction_date.toLocalDateTime());
        dto.setCreatorID(from_acc_id);
        return dto;
    }
}
