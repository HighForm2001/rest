package com.ocbc_rpp.rest.services;

import com.ocbc_rpp.rest.assemblers.TransactionModelAssembler;
import com.ocbc_rpp.rest.assemblers.TransactionReportModelAssembler;
import com.ocbc_rpp.rest.controllers.TransactionController;
import com.ocbc_rpp.rest.exceptions.CustomerNotFoundException;
import com.ocbc_rpp.rest.exceptions.NoSuchPageException;
import com.ocbc_rpp.rest.exceptions.TransactionNotFoundException;
import com.ocbc_rpp.rest.models.Customer;
import com.ocbc_rpp.rest.models.Transaction;
import com.ocbc_rpp.rest.models.TransactionReportSum;
import com.ocbc_rpp.rest.models.dto.TransactionDto;
import com.ocbc_rpp.rest.models.request.TransactionCreateRequest;
import com.ocbc_rpp.rest.repositories.CustomerRepository;
import com.ocbc_rpp.rest.repositories.TransactionRepository;
import org.springframework.data.domain.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class    TransactionService {
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final TransactionModelAssembler assembler;
    private final TransactionReportModelAssembler reportAssembler;

    public TransactionService(TransactionRepository repo, CustomerRepository cusRepo, TransactionModelAssembler assembler, TransactionReportModelAssembler reportAssembler){
        this.transactionRepository = repo;
        this.customerRepository = cusRepo;
        this.assembler = assembler;
        this.reportAssembler = reportAssembler;
    }

    public ResponseEntity<?> newTransaction(TransactionCreateRequest transaction) throws CustomerNotFoundException {

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
        return transactionRepository.findAll()
                .stream()
                .collect(Collectors
                        .groupingBy(report-> Arrays.asList(report.getCreator().getName(),report.getCreator().getAccountNo(),report.getDate()),Collectors
                                .summingDouble(Transaction::getAmount)))
                .entrySet()
                .stream()
                .map(entry->{
                    TransactionReportSum t = new TransactionReportSum();
                    t.setAmount(entry.getValue());
                    t.setName(entry.getKey().get(0).toString());
                    t.setId((Long) entry.getKey().get(1));
                    t.setDateTime((LocalDate)entry.getKey().get(2));
                    return t;    }
                ).sorted(Comparator.comparingLong(TransactionReportSum::getId))
                .toList();
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
    public CollectionModel<EntityModel<TransactionDto>> pageAndSort(int page) throws NoSuchPageException {

        Sort.TypedSort<Transaction> type = Sort.sort(Transaction.class);
        Sort sort = type.by(Transaction::getAmount).ascending();
        Page<Transaction> all = transactionRepository.findAll(PageRequest.of(page,5,sort));
        List<EntityModel<TransactionDto>> transactions = assembler.toDtoList(all.getContent())
                .stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.
                of(transactions,
                        linkTo(methodOn(TransactionController.class)
                                .pageAndSort(page))
                                .withSelfRel());
    }

    public CollectionModel<EntityModel<TransactionDto>> pageAndSortMultiple(int page) throws NoSuchPageException {
        Sort.TypedSort<Transaction> type = Sort.sort(Transaction.class);
        Sort sort = type.by(Transaction::getAmount).ascending().and(type.by(Transaction::getTransactionDate).descending());
        Page<Transaction> all = transactionRepository.findAll(PageRequest.of(page,3,sort));
        List<EntityModel<TransactionDto>> transactions = assembler.toDtoList(all.getContent())
                .stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.
                of(transactions,
                        linkTo(methodOn(TransactionController.class)
                                .pageAndSortMultiple(page))
                                .withSelfRel());
    }



    private <T> CollectionModel<EntityModel<T>> generate_CollectionModel(List<EntityModel<T>> list, Link selfLink){
        Link link1 = linkTo(methodOn(TransactionController.class).all()).withRel("api/transaction");
        Link link2 = linkTo(methodOn(TransactionController.class).sumTotal()).withRel("api/transaction/total");
        List<Link> links = new LinkedList<>();
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


    public CollectionModel<EntityModel<TransactionReportSum>> JpqlTest() {
        List<TransactionReportSum> report = transactionRepository.leftJoinAndSumJPQL();
        List<EntityModel<TransactionReportSum>> reportEntity = report.stream().map(reportAssembler::toModel).toList();
        return CollectionModel.of(reportEntity,linkTo(methodOn(TransactionController.class).JpqlTest()).withSelfRel());
    }

    public CollectionModel<EntityModel<TransactionDto>> pageSlice(String name,int page) throws NoSuchPageException {
        Pageable paging = PageRequest.of(page,2);
        Slice<Transaction> slice = transactionRepository.findByCreator_Name(name,paging);
        List<EntityModel<TransactionDto>> list = assembler.toDtoList(slice.getContent()).stream().map(assembler::toModel).toList();
        return CollectionModel.of(list,linkTo(methodOn(TransactionController.class).pageSlice(page,name)).withSelfRel());
    }

    public CollectionModel<EntityModel<TransactionReportSum>> QueryTest() {
        List<TransactionReportSum> report= transactionRepository.findGroupByReportWithNativeQuery()
                .stream()
                .map(iReport-> new TransactionReportSum(iReport.getName(),iReport.getId(),iReport.getDate(),iReport.getAmount()))
                .toList();
        List<EntityModel<TransactionReportSum>> entityModels = report.stream().map(reportAssembler::toModel).toList();
        return CollectionModel.of(entityModels,linkTo(methodOn(TransactionController.class).QueryTest()).withSelfRel());
    }
}
