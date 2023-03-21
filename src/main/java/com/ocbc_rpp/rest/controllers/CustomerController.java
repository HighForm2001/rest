package com.ocbc_rpp.rest.controllers;

import com.ocbc_rpp.rest.models.Customer;
import com.ocbc_rpp.rest.exceptions.CustomerNotFoundException;
import com.ocbc_rpp.rest.models.CustomerInfo;
import com.ocbc_rpp.rest.models.dto.CustomerDto;
import com.ocbc_rpp.rest.services.CustomerService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
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
    public ResponseEntity<?> newCustomer(@RequestBody Customer customer) {
        return service.newCustomer(customer);
    }

    @GetMapping("/transaction") // left join
    public CollectionModel<EntityModel<Customer>> all_with_transaction(){
        return service.all_with_transaction();
    }
    @GetMapping("/didTransaction")
    public CollectionModel<EntityModel<Customer>> did_Transaction(){
        return service.did_Transaction();
    }

    @GetMapping("/testCaseNative")
    public CollectionModel<EntityModel<CustomerInfo>> test_case_native(){
        return service.test_case_native();
    }
    @GetMapping("/testCaseJPQL")
    public CollectionModel<EntityModel<CustomerInfo>> test_case_jpql(){
        return service.test_case_jpql();
    }

    @GetMapping("/testCase/page={page}")
    public CollectionModel<EntityModel<CustomerInfo>> test_case_page(@PathVariable int page) {
        return service.test_case_page(page);
    }
    @GetMapping("/testCaseJPA")
    public CollectionModel<EntityModel<CustomerInfo>> test_case_jpa(){
        return service.test_case_jpa();
    }

    // Query here! incase deleted.
    /*
    select case
    when count(c.account_no)>0 then true
    else false end
    from customer_t c inner join
    transaction_t t on c.account_no = t.from_acc_id
    where c.account_no =4
     */
    @GetMapping("/checkMakeTransaction={id}")
    public boolean checkMakeTransaction(@PathVariable Long id){
        return service.checkMakeTransaction(id);
    }
}
