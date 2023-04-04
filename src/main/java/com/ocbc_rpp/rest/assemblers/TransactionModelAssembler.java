package com.ocbc_rpp.rest.assemblers;

import com.ocbc_rpp.rest.controllers.TransactionController;
import com.ocbc_rpp.rest.exceptions.TransactionNotFoundException;
import com.ocbc_rpp.rest.models.Transaction;
import com.ocbc_rpp.rest.models.dto.TransactionDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TransactionModelAssembler implements
        RepresentationModelAssembler<TransactionDto, EntityModel<TransactionDto>> {
    @Override
    public @NotNull EntityModel<TransactionDto> toModel(@NotNull TransactionDto transaction) {
        try {
            return EntityModel.of(transaction,
                    linkTo(methodOn(TransactionController.class)
                            .one(transaction.getTransactionReference())).withSelfRel(),
                    linkTo(methodOn(TransactionController.class)
                            .all()).withRel("api/transaction"));
        } catch (TransactionNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public List<TransactionDto> toDtoList(List<Transaction> transactions){
        return transactions.stream()
                .map(Transaction::toDTO).toList();
    }

}


