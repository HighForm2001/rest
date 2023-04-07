package com.ocbc_rpp.rest;

import com.ocbc_rpp.rest.config.SpringDataJpaConfiguration;
import com.ocbc_rpp.rest.models.Customer;
import com.ocbc_rpp.rest.models.Transaction;
import com.ocbc_rpp.rest.repositories.CustomerRepository;
import com.ocbc_rpp.rest.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = SpringDataJpaConfiguration.class)
class RestApplicationTests {


    @Test
    void testCustomerRepository(@Autowired CustomerRepository customerRepository){
        List<Customer> customers = customerRepository.findAll();
        customers.forEach(System.out::println);
    }

    @Test
    void testTransactionRepository(@Autowired TransactionRepository transactionRepository){
        List<Transaction> transactions = transactionRepository.findAll();
        transactions.forEach(System.out::println);
    }


}
