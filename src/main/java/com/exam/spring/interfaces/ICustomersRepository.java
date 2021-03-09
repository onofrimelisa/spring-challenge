package com.exam.spring.interfaces;

import com.exam.spring.dto.CustomerDTO;
import com.exam.spring.dto.CustomerRequestDTO;

import java.util.Optional;

public interface ICustomersRepository {
    CustomerDTO addCustomer(CustomerRequestDTO customerRequestDTO);
    Optional<CustomerDTO> getCustomerByDNI(String dni);
}
