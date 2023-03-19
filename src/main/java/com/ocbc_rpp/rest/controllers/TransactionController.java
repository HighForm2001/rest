package com.ocbc_rpp.rest.controllers;

import com.ocbc_rpp.rest.models.Transaction;
import com.ocbc_rpp.rest.models.TransactionReportSum;
import com.ocbc_rpp.rest.models.dto.CustomerDto;
import com.ocbc_rpp.rest.models.dto.TransactionDto;
import com.ocbc_rpp.rest.models.request.TransactionCreateRequest;
import com.ocbc_rpp.rest.exceptions.CustomerNotFoundException;
import com.ocbc_rpp.rest.exceptions.TransactionNotFoundException;
import com.ocbc_rpp.rest.services.TransactionService;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionService service;
    public TransactionController(TransactionService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> newTransaction(@RequestBody @NotNull TransactionCreateRequest transaction) throws CustomerNotFoundException, TransactionNotFoundException {
        return service.newTransaction(transaction);
    }


    @GetMapping("/id={id}")
    public EntityModel<TransactionDto> one(@PathVariable Long id) throws TransactionNotFoundException {
        return service.one(id);
    }

    @GetMapping("filter/left={id}")
    public CollectionModel<EntityModel<TransactionDto>> findTransactionLeft(@PathVariable Long id) throws TransactionNotFoundException, CustomerNotFoundException {
        return service.findTransactionLeft(id);
    }

    @GetMapping("/filter/left={id}/exclude={id2}")
    public CollectionModel<EntityModel<TransactionDto>> findTransactionWithout(@PathVariable Long id, @PathVariable Long id2) throws  CustomerNotFoundException{
        return service.findTransactionWithout(id,id2);
    }
    @GetMapping("/total/id={id}/date")
    public CollectionModel<EntityModel<TransactionReportSum>> sumTotalDateWithId(@PathVariable Long id) throws  CustomerNotFoundException{
        return service.sumTotalDateWithId(id);
    }
    @GetMapping("/total")
    public CollectionModel<EntityModel<TransactionReportSum>> sumTotal(){
        return service.sumTotal();
    }

    @GetMapping
    public CollectionModel<EntityModel<TransactionDto>> all(){
        return service.all();
    }

    @GetMapping("/innerJoin")
    public CollectionModel<EntityModel<CustomerDto>> testJoin() throws CustomerNotFoundException {
        return service.testJoin();
    }
    @GetMapping("/leftJoin")
    public CollectionModel<EntityModel<CustomerDto>> testLeftJoin() throws CustomerNotFoundException{
        return service.leftJoin();
    }

    @GetMapping("/pageAndSort={page}")
    public CollectionModel<EntityModel<TransactionDto>> pageAndSort(@PathVariable int page){
        return service.pageAndSort(page);
    }
}
