package com.ocbc_rpp.rest.repositories;

import com.ocbc_rpp.rest.models.Customer;
import com.ocbc_rpp.rest.models.Transaction;
import com.ocbc_rpp.rest.models.TransactionReportSum;
import com.ocbc_rpp.rest.models.TransactionReportSumInterface;
import com.ocbc_rpp.rest.models.dto.TransactionDto;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    @Query(value = "select new com.ocbc_rpp.rest.models.TransactionReportSum" +
            "(c.name,c.accountNo, cast(t.transactionDate as localdate)," +
            "SUM(case when t is null then 0 else t.amount end)) " +
            "FROM Customer c left join c.transactionsMade t group by " +
            "c.accountNo, c.name, cast(t.transactionDate as localdate ) " +
            "order by c.accountNo")
    List<TransactionReportSum> leftJoinAndSumJPQL();


    //Note: The getFunction in TransactionReportSumInterface must get the
    //      exact name of the query. FOr example, getAccount_No to get c.account_no
    //      getTotal_Amount to get total_amount.
    //      if the get method is not same wit the name in the query, it will return null.
    @Query(value = "SELECT c.name, c.account_no, DATE(t.transaction_date)" +
            ", COALESCE(SUM(t.amount),0) as total_amount FROM customer_t c" +
            " left join transaction_t t on c.account_no = t.from_acc_id Group by" +
            " c.account_no, c.name, Date(t.transaction_date) Order by c.account_no",nativeQuery = true)
    List<TransactionReportSumInterface> findGroupByReportWithNativeQuery();

    List<Transaction> findByCreatorAndReceiverNot(Customer receiver, Customer creator);

    Slice<Transaction> findByCreator_Name(String name, Pageable page);
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
