package com.ocbc_rpp.rest.repositories;

import com.ocbc_rpp.rest.models.Customer;
import com.ocbc_rpp.rest.models.CustomerInfo;
import com.ocbc_rpp.rest.models.CustomerInfoInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    List<Customer> findAllByTransactionsMadeIsNotNull();

//    List<Customer> findAllByTransactionsMadeNotInOrTransactionsMadeIsNull(List<Transaction> tran1, List<Transaction> tran2);

    @Query(value = "SELECT c.name, c.account_no, c.phone_no, c.balance, case " +
            "when (c.phone_no like '60%') then 'MY' " +
            "when (c.phone_no like '65%') then 'SG' " +
            "when (c.phone_no like '61%') then 'AU' " +
            "else 'Other' end as Code from customer_t c", nativeQuery = true)
    List<CustomerInfoInterface> findCustomerInfoNative();

    @Query(value = "SELECT new com.ocbc_rpp.rest.models.CustomerInfo(" +
            "c.accountNo," +
            "c.name," +
            "c.phoneNo," +
            "c.balance," +
            "case " +
            "when c.phoneNo like '60%' then 'MY'" +
            "when c.phoneNo like '61%' then 'AU'" +
            "when c.phoneNo like '65%' then 'SG'" +
            "else 'Other' end) from Customer c")
    List<CustomerInfo> findCustomerInfoJpql();

    @Query(value = "SELECT new com.ocbc_rpp.rest.models.CustomerInfo(" +
            "c.accountNo," +
            "c.name," +
            "c.phoneNo," +
            "c.balance," +
            "case " +
            "when c.phoneNo like '60%' then 'MY'" +
            "when c.phoneNo like '61%' then 'AU'" +
            "when c.phoneNo like '65%' then 'SG'" +
            "else 'Other' end) from Customer c")
    Page<CustomerInfo> findCustomerInfoJpql(Pageable pageable);

//    List<Customer> findAllByTransactionsMadeInOrNotIn(List<Transaction> transactions);
}
