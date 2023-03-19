package com.ocbc_rpp.rest.repositories;

import com.ocbc_rpp.rest.models.Customer;
import com.ocbc_rpp.rest.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    List<Transaction> findByCreator(Customer creator);

    List<Transaction> findAllByCreatorIn(List<Customer> creator);

    List<Transaction> findAllByCreatorNotInOrCreatorIn(List<Customer> creators, List<Customer> creators2);

    List<Transaction> findByCreatorAndReceiverNot(Customer receiver, Customer creator);

    //    List<Transaction>(Long id);

//    @Query(value = "select * from transaction_t inner join customer_t on transaction_t.from_acc_id = customer_t.account_no;",nativeQuery = true)
//    List<Transaction> findByFrom_acc_id(Long id);
//
//    @Query(value = "select * from transaction_t left join customer_t on transaction_t.from_acc_id = customer_t.account_no",nativeQuery = true)
//    List<Transaction>findByFrom_acc_id_left(Long id);
//
//    @Query(value = "select * from transaction_t as t where t.from_acc_id = ? and t.to_acc_id <> ?",nativeQuery = true)
//    List<Transaction>findByFrom_acc_id_AndTo_acc_id(Long from_id,Long to_id);
//
//    @Query(value = "select date(t.transaction_date), sum(t.amount)  as transfered_amount from transaction_t as t where t.from_acc_id=? group by t.from_acc_id , date(t.transaction_date);",nativeQuery = true)
//    List<TransactionReportSum> findTotalTransfered(Long id);
}
