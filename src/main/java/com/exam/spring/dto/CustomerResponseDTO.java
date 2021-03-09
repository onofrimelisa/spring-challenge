package com.exam.spring.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerResponseDTO extends ResponseDTO{
    private CustomerDTO customer;
}
