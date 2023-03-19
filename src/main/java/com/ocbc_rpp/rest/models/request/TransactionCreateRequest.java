package com.ocbc_rpp.rest.models.request;

public class TransactionCreateRequest {
    private double amount;
    private String currency;
    private long from_acc;
    private long to_acc;

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public long getFrom_acc() {
        return from_acc;
    }

    public long getTo_acc() {
        return to_acc;
    }
}
