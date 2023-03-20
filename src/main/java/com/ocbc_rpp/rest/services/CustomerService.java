package com.ocbc_rpp.rest.services;

import com.ocbc_rpp.rest.assemblers.CustomerModelAssembler;
import com.ocbc_rpp.rest.controllers.CustomerController;
import com.ocbc_rpp.rest.models.dto.CustomerDto;
import com.ocbc_rpp.rest.repositories.CustomerRepository;
import com.ocbc_rpp.rest.models.Customer;
import com.ocbc_rpp.rest.exceptions.CustomerNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CustomerService {
    private final CustomerRepository repository;

    private final CustomerModelAssembler assembler;
    public CustomerService(CustomerRepository repository, CustomerModelAssembler assembler){
        this.repository = repository;
        this.assembler = assembler;
    }

    public CollectionModel<EntityModel<CustomerDto>> all(){
        List<EntityModel<CustomerDto>> customers = assembler.toDto(repository.findAll()).stream()
                .map(assembler::toModel)
                .toList();
        return CollectionModel.of(customers,
                linkTo(methodOn(CustomerController.class).all()).withSelfRel());
    }

    public EntityModel<CustomerDto> one(Long id) throws CustomerNotFoundException {
        Customer customer = repository.findById(id).orElseThrow(()->new CustomerNotFoundException(id));
        return assembler.toModel(customer.toDto());
    }

    public ResponseEntity<?> newCustomer(Customer customer) {
        EntityModel<CustomerDto> newCustomer = assembler.toModel(repository.save(customer).toDto());
        return ResponseEntity
                .created(newCustomer
                        .getRequiredLink(IanaLinkRelations.SELF)
                        .toUri())
                .body(newCustomer);
    }

    public CollectionModel<EntityModel<Customer>> all_with_transaction(){
        List<EntityModel<Customer>> customer = repository
                .findAll()
                .stream()
                .map(assembler::toModel)
                .toList();
        return CollectionModel.
                of(customer,linkTo(methodOn(CustomerController.class)
                        .all_with_transaction())
                        .withSelfRel());
    }

    public CollectionModel<EntityModel<Customer>> did_Transaction(){
        List<EntityModel<Customer>> customer = repository.findAllByTransactionsMadeIsNotNull()
                .stream().map(assembler::toModel).toList();
        return CollectionModel.of(customer,linkTo(methodOn(CustomerController.class).did_Transaction()).withSelfRel());
    }
}
