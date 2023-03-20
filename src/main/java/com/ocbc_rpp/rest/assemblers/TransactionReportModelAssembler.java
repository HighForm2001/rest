package com.ocbc_rpp.rest.assemblers;

import com.ocbc_rpp.rest.controllers.TransactionController;
import com.ocbc_rpp.rest.models.TransactionReportSum;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TransactionReportModelAssembler implements RepresentationModelAssembler<TransactionReportSum, EntityModel<TransactionReportSum>> {
    @Override
    public @NotNull EntityModel<TransactionReportSum> toModel(@NotNull TransactionReportSum report) {
        try{
            return EntityModel.of(report,linkTo(methodOn(TransactionController.class).sumTotalDateWithId(report.getId())).withSelfRel(),
                    linkTo(methodOn(TransactionController.class).sumTotal()).withRel("/api/transaction/total"));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
