package com.ocbc_rpp.rest.models;

import com.ocbc_rpp.rest.models.dto.TransactionDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "transaction_t")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transaction_reference;
    private double amount;
    private String currency;
    @Column(name = "transaction_id")
    private int transactionID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="from_acc_id")
    private Customer creator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="to_acc_id")
    private Customer receiver;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    public void setTransaction_date(){
        this.transactionDate = LocalDateTime.now();
    }

    public TransactionDto toDTO(){
        return new TransactionDto(this.getTransaction_reference(),this.amount,this.currency,this.transactionDate,this.getCreator().getAccountNo(),this.receiver.getAccountNo());
    }
}
