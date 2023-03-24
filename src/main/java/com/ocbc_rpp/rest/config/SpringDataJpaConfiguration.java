package com.ocbc_rpp.rest.config;


import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.ocbc_rpp.rest.repositories")
@EnableTransactionManagement
public class SpringDataJpaConfiguration {
    private final String username = "postgres";
    private final String password = "1234";
    private final String db_name = "transaction_db";
    private final String db_link = "127.0.0.1";
//    private final String db_link = "localhost";

    private final String port = "5432";
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://"+db_link+":" + port +"/"+db_name)
                .username(username)
                .password(password)
                .build();
    }

}
