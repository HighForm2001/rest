package com.ocbc_rpp.rest.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ocbc_rpp.rest.models.dto.CustomerDto;
import com.ocbc_rpp.rest.util.LazyFieldsFilter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="customer_t")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_no")
    private Long accountNo;
    private String name;
    private String phoneNo; //phone_no
    private double balance;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "creator")
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = LazyFieldsFilter.class)
    private List<Transaction>
            transactionsMade;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "receiver")
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = LazyFieldsFilter.class)
    private List<Transaction>
            transactionsReceive;


    public CustomerDto toDto(){
        return new CustomerDto(this.accountNo,this.name,this.phoneNo,this.balance,this.transactionsMade,this.transactionsReceive);
    }
}
