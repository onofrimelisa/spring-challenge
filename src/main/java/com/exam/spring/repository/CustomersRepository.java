package com.exam.spring.repository;

import com.exam.spring.dto.CustomerDTO;
import com.exam.spring.dto.CustomerRequestDTO;
import com.exam.spring.interfaces.ICustomersRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class CustomersRepository implements ICustomersRepository {
    private Map<Integer, CustomerDTO> customers = new HashMap<>();


    @Override
    public CustomerDTO addCustomer(CustomerRequestDTO customerRequestDTO) {
        CustomerDTO newCustomer = new CustomerDTO();
        newCustomer.setDni(customerRequestDTO.getDni());
        newCustomer.setLastname(customerRequestDTO.getLastname());
        newCustomer.setName(customerRequestDTO.getName());
        this.customers.put(newCustomer.getCustomerId(), newCustomer);
        System.out.println(this.customers.size());
        return newCustomer;
    }

    @Override
    public Optional<CustomerDTO> getCustomerByDNI(String dni) {
        return this.customers.values().stream().filter(customerDTO -> customerDTO.getDni().equals(dni)).findFirst();
    }
}