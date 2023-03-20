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
    private LocalDate dateTime;
    private Double amount;
    public TransactionReportSum(String name, Long id, LocalDateTime dateTime, Double amount){
        this.name = name;
        this.id = id;
        this.dateTime = dateTime.toLocalDate();
        this.amount = amount;
    }
}
