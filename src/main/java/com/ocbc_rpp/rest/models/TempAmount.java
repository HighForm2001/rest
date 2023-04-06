package com.ocbc_rpp.rest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "temp_amount")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempAmount {

    @Id
    @Column(name = "account_no")
    private Long accountNo;

    private BigDecimal amount;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "account_no")
    @JsonIgnore
    private Customer customer;

    private String name;
    public TempAmount(Long accountNo, BigDecimal amount, String name){
        this.accountNo = accountNo;
        this.amount = amount;
        this.name = name;
    }

}
