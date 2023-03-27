package com.ocbc_rpp.rest.assemblers;

import com.ocbc_rpp.rest.controllers.CustomerController;
import com.ocbc_rpp.rest.exceptions.CustomerNotFoundException;
import com.ocbc_rpp.rest.models.Customer;
import com.ocbc_rpp.rest.models.CustomerInfo;
import com.ocbc_rpp.rest.models.dto.CustomerDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CustomerModelAssembler implements
        RepresentationModelAssembler<CustomerDto, EntityModel<CustomerDto>> {
    @Override
    public @NotNull EntityModel<CustomerDto> toModel(@NotNull CustomerDto customer) {
        try{
            return EntityModel.
                    of(customer,linkTo(methodOn(CustomerController.class)
                                    .one(customer.getAccountNo())).withSelfRel(),
                    linkTo(methodOn(CustomerController.class)
                            .all()).withRel("/api/customers"));
        }catch (CustomerNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    public EntityModel<Customer> toModel(Customer customer) {
        try{
            return EntityModel.of(customer,
                    linkTo(methodOn(CustomerController.class)
                            .one(customer.getAccountNo())).withSelfRel(),
                    linkTo(methodOn(CustomerController.class)
                            .all()).withRel("/api/customers"));
        }catch (CustomerNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    public EntityModel<CustomerInfo> toModel(CustomerInfo customer){
        try{
            return EntityModel.of(customer,
                    linkTo(methodOn(CustomerController.class)
                            .one(customer.getAccountNo())).withSelfRel(),
                    linkTo(methodOn(CustomerController.class)
                            .test_case_native()).withRel("/api/customers/testCaseNative"),
                    linkTo(methodOn(CustomerController.class)
                            .test_case_jpql()).withRel("/api/customers/testCaseJPQL"));
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<CustomerDto> toDto(List<Customer> customers){
        return customers.stream().map(Customer::toDto).toList();
    }
}
