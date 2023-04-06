package com.ocbc_rpp.rest.repositories;

import com.ocbc_rpp.rest.models.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends
        JpaRepository<Transaction,Long>{

    List<Transaction> findByCreatorAndReceiverNot(Customer creator, Customer receiver);

    List<Transaction>findAllByCreator_AccountNo(Long id);

    List<Transaction>findAllByCreator_AccountNoAndTransactionDateBetween(Long id, LocalDateTime start,LocalDateTime end);

    List<Transaction> findAllByCreator_AccountNoAndAmountGreaterThanEqual(Long id, Double amount);

    boolean existsTransactionByCreator_AccountNo(Long id);

    @Query(value = "select new com.ocbc_rpp.rest.models.TransactionReportSum" +
            "(c.name,c.accountNo, cast(t.transactionDate as localdate)," +
            "SUM(case when t is null then 0 else t.amount end)) " +
            "FROM Customer c left join c.transactionsMade t group by " +
            "c.accountNo, c.name, cast(t.transactionDate as localdate ) " +
            "order by c.accountNo")
    List<TransactionReportSum> groupAndSumJpql();


    //Note: The getFunction in TransactionReportSumInterface must get the
    //      exact name of the query. FOr example, getAccount_No to get c.account_no
    //      getTotal_Amount to get total_amount.
    //      if the get method is not same wit the name in the query, it will return null.
    @Query(value = "SELECT c.name, c.account_no as acc, DATE(t.transaction_date)" +
            ", COALESCE(SUM(t.amount),0) as total_amount FROM customer_t c" +
            " left join transaction_t t on c.account_no = t.from_acc_id Group by" +
            " c.account_no, c.name, Date(t.transaction_date) Order by c.account_no",nativeQuery = true)
    List<TransactionReportSumInterface> findGroupByReportWithNativeQuery();

    @Procedure
    List<Transaction> my_function();

    @Procedure
    List<Transaction> test_function3(Integer id);

    Slice<Transaction> findByCreator_Name(String name, Pageable page);
//    Page<Transaction> findByCreator_Name(String name, Pageable page);

    // Temporary Table


}
