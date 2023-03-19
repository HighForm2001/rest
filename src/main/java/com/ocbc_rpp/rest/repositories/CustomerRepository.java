package com.ocbc_rpp.rest.repositories;

import com.ocbc_rpp.rest.models.Customer;
import com.ocbc_rpp.rest.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    List<Customer> findAllByTransactionsMadeIsNotNull();

//    List<Customer> findAllByTransactionsMadeNotInOrTransactionsMadeIsNull(List<Transaction> tran1, List<Transaction> tran2);
    List<Customer> findAllByTransactionsMadeNotIn(List<Transaction> transactions);
    List<Customer> findAllByTransactionsMadeIsNotNullOrTransactionsMadeIsNull();

//    List<Customer> findAllByTransactionsMadeInOrNotIn(List<Transaction> transactions);
}
