package com.ocbc_rpp.rest.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionReportSum {
    private String name;
    private Long id;
    private LocalDate date;
    private double amount;
    public TransactionReportSum(String name, Long id, LocalDateTime dateTime, double amount){
        this.name = name;
        this.id = id;
        this.date = dateTime.toLocalDate();
        this.amount = amount;
    }
}
