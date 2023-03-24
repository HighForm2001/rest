package com.ocbc_rpp.rest.services;

import com.ocbc_rpp.rest.assemblers.CustomerModelAssembler;
import com.ocbc_rpp.rest.controllers.CustomerController;
import com.ocbc_rpp.rest.models.CustomerInfo;
import com.ocbc_rpp.rest.models.dto.CustomerDto;
import com.ocbc_rpp.rest.repositories.CustomerRepository;
import com.ocbc_rpp.rest.models.Customer;
import com.ocbc_rpp.rest.exceptions.CustomerNotFoundException;
import com.ocbc_rpp.rest.repositories.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final TransactionRepository transactionRepository;

    private final CustomerModelAssembler assembler;

    public CustomerService(CustomerRepository repository, CustomerModelAssembler assembler, TransactionRepository transactionRepository) {
        this.repository = repository;
        this.assembler = assembler;
        this.transactionRepository = transactionRepository;
    }

    public CollectionModel<EntityModel<CustomerDto>> all() {
        List<EntityModel<CustomerDto>> customers = assembler.toDto(repository.findAll()).stream()
                .map(assembler::toModel)
                .toList();
        return CollectionModel.of(customers,
                linkTo(methodOn(CustomerController.class).all()).withSelfRel());
    }

    public EntityModel<CustomerDto> one(Long id) throws CustomerNotFoundException {
        Customer customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
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

    public CollectionModel<EntityModel<Customer>> all_with_transaction() {
        List<EntityModel<Customer>> customer = repository
                .findAll()
                .stream()
                .map(assembler::toModel)
                .toList();
        return CollectionModel.
                of(customer, linkTo(methodOn(CustomerController.class)
                        .all_with_transaction())
                        .withSelfRel());
    }

    public CollectionModel<EntityModel<Customer>> did_Transaction() {
        List<EntityModel<Customer>> customer = repository.findAllByTransactionsMadeIsNotNull()
                .stream().map(assembler::toModel).toList();
        return CollectionModel.of(customer, linkTo(methodOn(CustomerController.class).did_Transaction()).withSelfRel());
    }

    public CollectionModel<EntityModel<CustomerInfo>> test_case_native() {
        List<EntityModel<CustomerInfo>> info = repository.findCustomerInfoNative()
                .stream()
                .map(iInfo ->
                        new CustomerInfo(iInfo.getAccount_No(), iInfo.getName(), iInfo.getPhone_No(), iInfo.getBalance(), iInfo.getCode()))
                .toList()
                .stream()
                .map(assembler::toModel)
                .toList();
        return CollectionModel.of(info, linkTo(methodOn(CustomerController.class).test_case_native()).withSelfRel());

    }

    public CollectionModel<EntityModel<CustomerInfo>> test_case_jpql() {
        List<EntityModel<CustomerInfo>> info = repository
                .findCustomerInfoJpql()
                .stream()
                .map(assembler::toModel)
                .toList();
        return CollectionModel.of(info, linkTo(methodOn(CustomerController.class).test_case_jpql()).withSelfRel());
    }

    public CollectionModel<EntityModel<CustomerInfo>> test_case_page(int page_size) {
        Sort.TypedSort<CustomerInfo> sort = Sort.sort(CustomerInfo.class);
        Sort type = sort.by(CustomerInfo::getAccountNo).descending().and(sort.by(CustomerInfo::getBalance).ascending());
        Page<CustomerInfo> page = repository.findCustomerInfoJpql(PageRequest.of(page_size, 5, type));
        List<EntityModel<CustomerInfo>> info = page.getContent().stream().map(assembler::toModel).toList();
        return CollectionModel.of(info, linkTo(methodOn(CustomerController.class).test_case_page(page_size)).withSelfRel());
    }

    public CollectionModel<EntityModel<CustomerInfo>> test_case_jpa() {
        List<CustomerInfo> info = repository.findAll()
                .stream()
                .map(customer -> {
                    String code = switch (customer.getPhoneNo().substring(0, 2)) {
                        case ("60") -> "MY";
                        case ("61") -> "AU";
                        case ("65") -> "SG";
                        default -> "Other";
                    };
                    return new CustomerInfo(customer.getAccountNo(), customer.getName(), customer.getPhoneNo()
                            , customer.getBalance(), code);
                }).toList();
        List<EntityModel<CustomerInfo>> modelInfo = info.stream().map(assembler::toModel).toList();
        return CollectionModel.of(modelInfo, linkTo(methodOn(CustomerController.class).test_case_jpa()).withSelfRel());
    }

    public boolean checkMakeTransaction(Long id) {
        return transactionRepository.existsTransactionByCreator_AccountNo(id);
    }
}
