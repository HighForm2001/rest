package com.ocbc_rpp.rest.assemblers;

import com.ocbc_rpp.rest.controllers.CustomerController;
import com.ocbc_rpp.rest.exceptions.CustomerNotFoundException;
import com.ocbc_rpp.rest.models.Customer;
import com.ocbc_rpp.rest.models.dto.CustomerDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CustomerModelAssembler implements RepresentationModelAssembler<CustomerDto, EntityModel<CustomerDto>> {
    @Override
    public EntityModel<CustomerDto> toModel(CustomerDto customer) {
        try{
            return EntityModel.of(customer,linkTo(methodOn(CustomerController.class).one(customer.getAccountNo())).withSelfRel(),
                    linkTo(methodOn(CustomerController.class).all()).withRel("/api/customers"));
        }catch (CustomerNotFoundException e){
            throw new RuntimeException(e);
        }
    }
    public List<CustomerDto> toDto(List<Customer> customers){
        return customers.stream().map(customer -> new CustomerDto(customer.getAccountNo(),customer.getName(),customer.getPhoneNo(),customer.getBalance())).toList();
    }
}
