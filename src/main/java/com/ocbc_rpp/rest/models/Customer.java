package com.ocbc_rpp.rest.models;

import com.ocbc_rpp.rest.models.dto.CustomerDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    @JoinColumn(name = "from_acc_id")
    private List<Transaction> transactionsMade;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiver")
    @JoinColumn(name = "to_acc_id")
    private List<Transaction> transactionsReceive;

//    @ManyToMany
//    @JoinTable(name = "transaction_customer_tb",
//    joinColumns = {@JoinColumn(name = "t_id")},
//    inverseJoinColumns = {@JoinColumn(name = "c_id")})
//


    public CustomerDto toDto(){
        return new CustomerDto(this.accountNo,this.name,this.phoneNo,this.balance);
    }
}
