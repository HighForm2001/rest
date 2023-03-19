package com.ocbc_rpp.rest.controllers;

import com.ocbc_rpp.rest.models.Customer;
import com.ocbc_rpp.rest.exceptions.CustomerNotFoundException;
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
    public ResponseEntity<?> newCustomer(@RequestBody Customer customer) throws CustomerNotFoundException {
        return service.newCustomer(customer);
    }
}
