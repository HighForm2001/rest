package com.ocbc_rpp.rest.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionReportSum {
    private Long id;
    private LocalDateTime dateTime;
    private double amount;
}
