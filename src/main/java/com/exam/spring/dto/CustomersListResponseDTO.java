package com.exam.spring.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CustomersListResponseDTO {
    private List<CustomerDTO> customers;
    private Integer total;
    private StatusCodeDTO statusCodeDTO;
}
