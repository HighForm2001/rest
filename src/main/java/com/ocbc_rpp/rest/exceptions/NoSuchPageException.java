package com.ocbc_rpp.rest.exceptions;

public class NoSuchPageException extends Exception{
    public NoSuchPageException(int page){
        super("Page " + page + " does not exist.");
    }
}
