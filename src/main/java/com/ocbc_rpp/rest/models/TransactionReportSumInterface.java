package com.ocbc_rpp.rest.models;

import java.time.LocalDate;

public interface TransactionReportSumInterface {
    String getName();
    Long getId();
    LocalDate getDate();
    double getAmount();
}
