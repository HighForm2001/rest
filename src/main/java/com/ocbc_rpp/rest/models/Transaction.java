package com.ocbc_rpp.rest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ocbc_rpp.rest.models.dto.TransactionDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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

    @ManyToOne
    @JoinColumn(name="from_acc_id")
    @JsonIgnore
    private Customer creator;

    @ManyToOne
    @JoinColumn(name="to_acc_id")
    @JsonIgnore
    private Customer receiver;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    public void setTransaction_date(){
        this.transactionDate = LocalDateTime.now();
    }
    public LocalDate getDate(){
        return transactionDate.toLocalDate();
    }

    public TransactionDto toDTO(){
        return new TransactionDto(
                this.getTransaction_reference()
                ,this.amount,this.currency
                ,this.transactionDate
                ,this.getCreator().getAccountNo()
                ,this.receiver.getAccountNo());
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transaction_reference=" + transaction_reference +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", transactionID=" + transactionID +
                ", creator=" + creator.getName() +
                ", receiver=" + receiver.getName() +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
