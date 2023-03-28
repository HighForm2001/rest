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

    @Query(value = "SELECT c.name, c.account_no, c.phone_no, c.balance, case " +
            "when (substring(c.phone_no,1,2) = '60') then 'MY' " +
            "when (substring(c.phone_no,1,2) = '65') then 'SG' " +
            "when (substring(c.phone_no,1,2) = '61') then 'AU' " +
            "else 'Other' end as Code from customer_t c", nativeQuery = true)
    List<CustomerInfoInterface> findCustomerInfoNative();

    @Query(value = "SELECT new com.ocbc_rpp.rest.models.CustomerInfo(" +
            "c.accountNo," +
            "c.name," +
            "c.phoneNo," +
            "c.balance," +
            "case " +
            "when substring(c.phoneNo,1,2) = '60' then 'MY'" +
            "when substring(c.phoneNo,1,2) = '61' then 'AU'" +
            "when substring(c.phoneNo,1,2) = '65' then 'SG'" +
            "else 'Other' end) from Customer c")
    List<CustomerInfo> findCustomerInfoJpql();

    @Query(value = "SELECT new com.ocbc_rpp.rest.models.CustomerInfo(" +
            "c.accountNo," +
            "c.name," +
            "c.phoneNo," +
            "c.balance," +
            "case " +
            "when substring(c.phoneNo,1,2) = '60' then 'MY'" +
            "when substring(c.phoneNo,1,2) = '61' then 'AU'" +
            "when substring(c.phoneNo,1,2) = '65' then 'SG'" + //dont use like
            "else 'Other' end) from Customer c")
    Page<CustomerInfo> findCustomerInfoJpql(Pageable pageable);

}
