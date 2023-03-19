package com.ocbc_rpp.rest.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private Long accountNo;
    private String name;
    private String phoneNo; //phone_no
    private double balance;
}
