package com.ocbc_rpp.rest;

import com.ocbc_rpp.rest.config.SpringDataJpaConfiguration;
import com.ocbc_rpp.rest.repositories.CustomerRepository;
import com.ocbc_rpp.rest.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = SpringDataJpaConfiguration.class)
class RestApplicationTests {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	TransactionRepository transactionRepository;

	@Test
	void contextLoads() {

	}

	@Test
	public void testJoin(){

	}

}
