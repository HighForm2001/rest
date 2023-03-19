package com.ocbc_rpp.rest.exceptions;

public class TransactionNotFoundException extends Exception{
    public TransactionNotFoundException(Long id){
        super("Transaction (reference number: " + id + ") not found.");
    }
}
