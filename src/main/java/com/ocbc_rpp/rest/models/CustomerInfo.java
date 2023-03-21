package com.ocbc_rpp.rest.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInfo {
    private Long accountNo;
    private String name;
    private String phone_no;
    private double balance;
    private String code;

}
