package com.ocbc_rpp.rest.controllers;

import com.ocbc_rpp.rest.exceptions.CustomerNotFoundException;
import com.ocbc_rpp.rest.exceptions.NoSuchPageException;
import com.ocbc_rpp.rest.exceptions.TransactionNotFoundException;
import com.ocbc_rpp.rest.models.TransactionReportSum;
import com.ocbc_rpp.rest.models.dto.TransactionDto;
import com.ocbc_rpp.rest.models.request.TransactionCreateRequest;
import com.ocbc_rpp.rest.services.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionService service;

    private final CsrfTokenRepository csrfTokenRepository;
    public TransactionController(TransactionService service, CsrfTokenRepository csrfTokenRepository){
        this.service = service;
        this.csrfTokenRepository = csrfTokenRepository;
    }

    @GetMapping
    public CollectionModel<EntityModel<TransactionDto>> all(){
        return service.all();
    }

    @GetMapping("/id={id}")
    public EntityModel<TransactionDto> one(@PathVariable Long id) throws TransactionNotFoundException {
        return service.one(id);
    }

    @PostMapping
    public ResponseEntity<?> newTransaction(@RequestBody TransactionCreateRequest transaction, @RequestHeader(name = "X-XSRF-TOKEN") String token_string, HttpServletRequest request)
            throws CustomerNotFoundException {
        CsrfToken token = csrfTokenRepository.loadToken(request);
        if (token == null || !token.getToken().equals(token_string))
            return ResponseEntity.badRequest().build();
        return service.newTransaction(transaction);

    }

    @GetMapping("/filter/id={id}/exclude={id2}")
    public CollectionModel<EntityModel<TransactionDto>>
    findTransactionWithout(@PathVariable Long id, @PathVariable Long id2)
            throws  CustomerNotFoundException{
        return service.findTransactionWithout(id,id2);
    }

    @GetMapping("/total")
    public CollectionModel<EntityModel<TransactionReportSum>> sumTotal(){
        return service.sumTotal();
    }

    @GetMapping("/total/id={id}")
    public CollectionModel<EntityModel<TransactionReportSum>> sumTotalDateWithId(@PathVariable Long id) throws  CustomerNotFoundException{
        return service.sumTotalDateWithId(id);
    }

    @GetMapping("/total/id={id}/amount={amount}")
    public CollectionModel<EntityModel<TransactionReportSum>> sumTotalDateWithIdAndAmount(@PathVariable Long id, @PathVariable double amount){
        return service.sumTotalDateWithIdAndAmount(id,amount);
    }

    @GetMapping("/total/id={id}/date={date_string}")
    public CollectionModel<EntityModel<TransactionReportSum>> sumTotalDateWithIdAndDate(@PathVariable Long id, @PathVariable String date_string)throws DateTimeParseException {

        return service.sumTotalDateWithIdAndDate(id,date_string);

    }

    @GetMapping("/testSpecification/id={id}/amount={amount}")
    public CollectionModel<EntityModel<TransactionReportSum>>
    testSpecification(@PathVariable Long id,@PathVariable double amount) {
        return service.testSpecification(id,amount);
    }

    @GetMapping("/JpqlLeftJoinSum")
    public CollectionModel<EntityModel<TransactionReportSum>> jpqlTest(){
        return service.JpqlTest();
    }

    @GetMapping("/NativeQLeftJoinSum")
    public CollectionModel<EntityModel<TransactionReportSum>> nativeQLeftJoinSum(){
        return service.nativeQLeftJoinSum();
    }

    @GetMapping("/testStoredProcedure")
    public CollectionModel<EntityModel<TransactionDto>> testStoredProcedure() {
        return service.testStoredProcedure();
    }

    @GetMapping("/testStoredProcedure={id}")
    public CollectionModel<EntityModel<TransactionDto>> testStoredProcedure2(@PathVariable Integer id) {
        return service.testStoredProcedure2(id);
    }

    @GetMapping("/pageAndSortOneColumn={page}")
    public CollectionModel<EntityModel<TransactionDto>>
    pageAndSort(@PathVariable int page) throws NoSuchPageException {
        if (page <0)
            throw new NoSuchPageException(page);
        return service.pageAndSort(page);
    }

    @GetMapping("/pageAndSortMultiple={page}")
    public CollectionModel<EntityModel<TransactionDto>>
    pageAndSortMultiple(@PathVariable int page) throws NoSuchPageException{
        if(page<0)
            throw new NoSuchPageException(page);
        return service.pageAndSortMultiple(page);
    }

    @GetMapping("/name={name}/pageSlice={page}")
    public CollectionModel<EntityModel<TransactionDto>>
    pageSlice(@PathVariable int page,@PathVariable String name)throws NoSuchPageException{
        if(page<0)
            throw new NoSuchPageException(page);
        return service.pageSlice(name,page);
    }












}
