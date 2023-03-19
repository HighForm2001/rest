package com.ocbc_rpp.rest.exceptions;

public class CustomerNotFoundException extends Exception{
    public CustomerNotFoundException(Long id){
        super("Customer (id: " + id + ") does not exist.");
    }
}
