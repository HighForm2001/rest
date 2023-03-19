package com.ocbc_rpp.rest.models;

import com.ocbc_rpp.rest.models.dto.CustomerDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="customer_t")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_no")
    private Long accountNo;
    private String name;
    private String phoneNo; //phone_no
    private double balance;

    @OneToMany(targetEntity = Transaction.class, mappedBy = "creator",fetch = FetchType.EAGER)
    private List<Transaction> transactionsMade;

    @OneToMany(targetEntity = Transaction.class, mappedBy = "receiver")
    private List<Transaction> transactionsReceive;

    public CustomerDto toDto(){
        return new CustomerDto(this.accountNo,this.name,this.phoneNo,this.balance);
    }
}
