package com.ocbc_rpp.rest.models.dto;

import com.ocbc_rpp.rest.models.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private Long accountNo;
    private String name;
    private String phoneNo; //phone_no
    private double balance;
    private List<Transaction> transactiosnMade;
    private List<Transaction> transactionsReceive;
}
