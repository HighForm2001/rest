package com.ocbc_rpp.rest.services;

import com.ocbc_rpp.rest.assemblers.CustomerModelAssembler;
import com.ocbc_rpp.rest.assemblers.TransactionModelAssembler;
import com.ocbc_rpp.rest.assemblers.TransactionReportModelAssembler;
import com.ocbc_rpp.rest.controllers.TransactionController;
import com.ocbc_rpp.rest.models.dto.CustomerDto;
import com.ocbc_rpp.rest.models.dto.TransactionDto;
import com.ocbc_rpp.rest.repositories.CustomerRepository;
import com.ocbc_rpp.rest.repositories.TransactionRepository;
import com.ocbc_rpp.rest.models.Customer;
import com.ocbc_rpp.rest.models.Transaction;
import com.ocbc_rpp.rest.models.TransactionReportSum;
import com.ocbc_rpp.rest.models.request.TransactionCreateRequest;
import com.ocbc_rpp.rest.exceptions.CustomerNotFoundException;
import com.ocbc_rpp.rest.exceptions.TransactionNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final TransactionModelAssembler assembler;
    private final TransactionReportModelAssembler reportAssembler;
    private final CustomerModelAssembler customerAssembler;

    public TransactionService(TransactionRepository repo, CustomerRepository cusRepo, TransactionModelAssembler assembler, TransactionReportModelAssembler reportAssembler,
                              CustomerModelAssembler customerAssembler){
        this.transactionRepository = repo;
        this.customerRepository = cusRepo;
        this.assembler = assembler;
        this.reportAssembler = reportAssembler;
        this.customerAssembler = customerAssembler;
    }

    public ResponseEntity<?> newTransaction(TransactionCreateRequest transaction) throws CustomerNotFoundException, TransactionNotFoundException {

        // assume both customer is valid (add validation on Customer Controller)
        Customer from = customerRepository.findById(transaction.getFrom_acc())
                .orElseThrow(()->new CustomerNotFoundException(transaction.getFrom_acc()));
        Customer to = customerRepository.findById(transaction.getTo_acc())
                .orElseThrow(()-> new CustomerNotFoundException(transaction.getTo_acc()));

        double afterAmount = from.getBalance() - transaction.getAmount();
        if (afterAmount < 0)
            return ResponseEntity.badRequest().body("Invalid Transaction. Amount exceeded account balance");

        from.setBalance(from.getBalance() - transaction.getAmount());
        to.setBalance(to.getBalance() + transaction.getAmount());
        customerRepository.save(from);
        customerRepository.save(to);

        Transaction newTransaction = new Transaction();
        newTransaction.setTransactionID(newTransaction.hashCode());
        newTransaction.setTransaction_date();
        newTransaction.setCreator(from);
        newTransaction.setReceiver(to);
        newTransaction.setAmount(transaction.getAmount());
        newTransaction.setCurrency(transaction.getCurrency());

        EntityModel<TransactionDto> newTransactionModel =assembler.toModel(transactionRepository.save(newTransaction).toDTO());
        return ResponseEntity
                .created(newTransactionModel.getRequiredLink(IanaLinkRelations.SELF)
                        .toUri()).body(newTransactionModel);

    }




    public EntityModel<TransactionDto> one(Long id) throws TransactionNotFoundException {
        Transaction transaction = transactionRepository
                .findById(id)
                .orElseThrow(()-> new TransactionNotFoundException(id));
        return assembler.toModel(transaction.toDTO());
    }

//    @GetMapping("/filter/inner{id}")
//    public CollectionModel<EntityModel<Transaction>> findTransactionInner(@PathVariable Long id) throws TransactionNotFoundException, CustomerNotFoundException {
//        Customer customer = customerRepository.findById(id).orElseThrow(()->new CustomerNotFoundException(id));
//        List<EntityModel<Transaction>> transactions = repository.findByCreatorOrTransactionID(customer, id).stream().map(assembler::toModel).toList();
//        return CollectionModel
//                .of(transactions,linkTo(methodOn(TransactionController.class).findTransactionInner(id)).withSelfRel());
//
//    }

    public CollectionModel<EntityModel<TransactionDto>> findTransactionLeft(Long id) throws TransactionNotFoundException, CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(()->new CustomerNotFoundException(id));
        List<EntityModel<TransactionDto>> transactions = assembler.toDtoList(transactionRepository.findByCreator(customer))
                .stream()
                .map(assembler::toModel)
                .toList();
        return  CollectionModel
                .of(transactions,linkTo(methodOn(TransactionController.class).findTransactionLeft(id)).withSelfRel());
    }


    public CollectionModel<EntityModel<TransactionDto>> findTransactionWithout(Long id, Long id2) throws  CustomerNotFoundException{
        Customer creator = customerRepository.findById(id).orElseThrow(()->new CustomerNotFoundException(id));
        Customer receiver = customerRepository.findById(id2).orElseThrow(()-> new CustomerNotFoundException(id2));
        List<EntityModel<TransactionDto>> transactions = assembler.toDtoList(transactionRepository.findByCreatorAndReceiverNot(creator,receiver))
                .stream()
                .filter(s-> !s.getCreatorID().equals(id2))
                .map(assembler::toModel)
                .toList();
        return CollectionModel.of(transactions,linkTo(methodOn(TransactionController.class).findTransactionWithout(id,id2)).withSelfRel());
    }

    public CollectionModel<EntityModel<TransactionReportSum>> sumTotalDateWithId(Long id) throws  CustomerNotFoundException{
//        List<EntityModel<TransactionReportSum>> balances = repository.findTotalTransfered(id)
//                .stream()
//                .map(reportAssembler::toModel)
//                .toList();
        List<TransactionReportSum> transactionReportSums = generateReport().stream().filter(s1->s1.getId().equals(id)).toList();
        List<EntityModel<TransactionReportSum>> reports = transactionReportSums.stream().map(reportAssembler::toModel).toList();
        Link self = linkTo(methodOn(TransactionController.class).sumTotalDateWithId(id)).withSelfRel();
        return generate_CollectionModel(reports,self);
    }

    public CollectionModel<EntityModel<TransactionReportSum>> sumTotal(){

        List<TransactionReportSum> transactionReportSums = generateReport();
        List<EntityModel<TransactionReportSum>> reports = transactionReportSums.stream().map(reportAssembler::toModel).toList();
        Link self = linkTo(methodOn(TransactionController.class).sumTotal()).withSelfRel();
        return generate_CollectionModel(reports,self);
    }

    private List<TransactionReportSum> generateReport(){
        List<TransactionReportSum> transactionReportSums = transactionRepository.findAll()
                .stream()
                .collect(Collectors
                        .groupingBy(report-> Arrays.asList(report.getCreator().getAccountNo(),report.getTransactionDate()),Collectors
                                .summingDouble(Transaction::getAmount)))
                .entrySet()
                .stream()
                .map(entry->{
                    TransactionReportSum t = new TransactionReportSum();
                    t.setAmount(entry.getValue());
                    t.setId(Long.parseLong(entry.getKey().get(0).toString()));
                    t.setDateTime((LocalDateTime)entry.getKey().get(1));
                    return t;    }
                ).sorted((s1,s2)->s1.getId().intValue() - s2.getId().intValue())
                .toList();
        return transactionReportSums;
    }
    public CollectionModel<EntityModel<TransactionDto>> all(){
        List<EntityModel<TransactionDto>> transactions = assembler.toDtoList(transactionRepository.findAll())
                .stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.
                of(transactions,
                        linkTo(methodOn(TransactionController.class)
                                .all())
                                .withSelfRel());
    }

    public CollectionModel<EntityModel<CustomerDto>> testJoin() throws CustomerNotFoundException{
        List<EntityModel<CustomerDto>> test1 = customerAssembler.toDto(customerRepository.findAllByTransactionsMadeIsNotNull())
                .stream().map(customerAssembler::toModel).toList();
        Link self = linkTo(methodOn(TransactionController.class).testJoin()).withSelfRel();
        return generate_CollectionModel(test1,self);
    }

    public CollectionModel<EntityModel<CustomerDto>> leftJoin() throws CustomerNotFoundException{
        List<Customer> test2 = customerRepository.findAllByTransactionsMadeIsNotNullOrTransactionsMadeIsNull();
        List<EntityModel<CustomerDto>> test = customerAssembler.toDto(test2)
                .stream().map(customerAssembler::toModel).toList();
        Link self = linkTo(methodOn(TransactionController.class).testLeftJoin()).withSelfRel();
        return  generate_CollectionModel(test,self);
    }

    private <T> CollectionModel<EntityModel<T>> generate_CollectionModel(List<EntityModel<T>> list, Link selfLink){
        Link link1 = linkTo(methodOn(TransactionController.class).all()).withRel("api/transaction");
        Link link2 = linkTo(methodOn(TransactionController.class).sumTotal()).withRel("api/transaction/total");
        List<Link> links = new LinkedList<Link>();
        links.add(link1);
        links.add(link2);
        if (link1.getHref().equals(selfLink.getHref())){
            links.remove(link1);
            links.add(selfLink);
        }else if (link2.getHref().equals(selfLink.getHref())){
            links.remove(link2);
            links.add(selfLink);

        }else{
            links.add(selfLink);
        }
        return CollectionModel.of(list,links);
    }
}
