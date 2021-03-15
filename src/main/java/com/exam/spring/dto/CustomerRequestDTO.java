package com.exam.spring.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequestDTO {
    private String dni;
    private String name;
    private String lastname;
    private String province;
}