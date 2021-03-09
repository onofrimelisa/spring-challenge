package com.exam.spring.dto;

import lombok.Getter;

@Getter
public class CustomerRequestDTO {
    private String dni;
    private String name;
    private String lastname;
    private String province;
}