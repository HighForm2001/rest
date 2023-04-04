package com.ocbc_rpp.rest.controllers;

import com.ocbc_rpp.rest.exceptions.CustomerNotFoundException;
import com.ocbc_rpp.rest.models.Customer;
import com.ocbc_rpp.rest.models.CustomerInfo;
import com.ocbc_rpp.rest.models.dto.CustomerDto;
import com.ocbc_rpp.rest.services.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService service;
    private final CsrfTokenRepository csrfTokenRepository;

    public CustomerController(CustomerService service, CsrfTokenRepository csrfTokenRepository) {
        this.service = service;
        this.csrfTokenRepository = csrfTokenRepository;
    }


    @GetMapping
    public CollectionModel<EntityModel<CustomerDto>> all(){
        return service.all();
    }

    @GetMapping("/id={id}")
    public EntityModel<CustomerDto> one(@PathVariable Long id) throws CustomerNotFoundException {
        return service.one(id);
    }

    @PostMapping
    public ResponseEntity<?> newCustomer(@RequestBody Customer customer, @RequestHeader(name = "X-XSRF-TOKEN") String token_string, HttpServletRequest request) {
        CsrfToken token = csrfTokenRepository.loadToken(request);
        if (token == null || !token.getToken().equals(token_string))
            return ResponseEntity.badRequest().build();
        return service.newCustomer(customer);
    }

    @GetMapping("/checkMakeTransaction={id}")
    public boolean checkMakeTransaction(@PathVariable Long id){
        return service.checkMakeTransaction(id);
    }

    @GetMapping("/testCaseJPA")
    public CollectionModel<EntityModel<CustomerInfo>> test_case_jpa(){
        return service.test_case_jpa();
    }

    @GetMapping("/caseSpecification")
    public CollectionModel<EntityModel<CustomerInfo>> caseSpecification() {
        return service.caseSpecification();
    }

    @GetMapping("/testCaseJPQL")
    public CollectionModel<EntityModel<CustomerInfo>> test_case_jpql(){
        return service.test_case_jpql();
    }

    @GetMapping("/testCaseNative")
    public CollectionModel<EntityModel<CustomerInfo>> test_case_native(){
        return service.test_case_native();
    }

    @GetMapping("/testCase/page={page}")
    public CollectionModel<EntityModel<CustomerInfo>> test_case_page(@PathVariable int page) {
        return service.test_case_page(page);
    }

    @GetMapping("/transaction") // left join
    public CollectionModel<EntityModel<Customer>> all_with_transaction(){
        return service.all_with_transaction();
    }

    @GetMapping("/didTransaction")
    public CollectionModel<EntityModel<Customer>> did_Transaction(){
        return service.did_Transaction();
    }

}
