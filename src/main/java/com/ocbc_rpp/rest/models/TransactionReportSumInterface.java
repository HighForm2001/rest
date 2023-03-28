package com.ocbc_rpp.rest.models;

import java.time.LocalDate;

public interface TransactionReportSumInterface {
    String getName();

    Long getacc();
    // getaccount_no();
    // getAccount_No();
    // getId();

    LocalDate getDate();

    double getTotal_Amount();
}
