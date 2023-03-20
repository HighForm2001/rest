package com.ocbc_rpp.rest.advicers;

import com.ocbc_rpp.rest.exceptions.CustomerNotFoundException;
import com.ocbc_rpp.rest.exceptions.NoSuchPageException;
import com.ocbc_rpp.rest.exceptions.TransactionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RecordNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String customerNotFound(CustomerNotFoundException exception){
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(TransactionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String transactionNotFound(TransactionNotFoundException exception){
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(NoSuchPageException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String pageNotFound(NoSuchPageException ex){
        return ex.getMessage();
    }
}
