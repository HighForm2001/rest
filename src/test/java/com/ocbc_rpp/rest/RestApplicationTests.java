package com.ocbc_rpp.rest;

import com.ocbc_rpp.rest.config.SpringDataJpaConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = SpringDataJpaConfiguration.class)
class RestApplicationTests {

}
