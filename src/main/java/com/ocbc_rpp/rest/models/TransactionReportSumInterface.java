package com.ocbc_rpp.rest.models;

import java.time.LocalDate;

public interface TransactionReportSumInterface {
    String getName();
    Long getAccount_No();
    LocalDate getDate();
    double getTotal_Amount();
}
