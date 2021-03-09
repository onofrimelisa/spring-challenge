package com.exam.spring.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {
    private static Integer counterCustomerId = 0;
    private Integer customerId;
    private String dni;
    private String name;
    private String lastname;
    private String province;

    public CustomerDTO() {
        this.customerId = counterCustomerId;
        counterCustomerId++;
    }
}