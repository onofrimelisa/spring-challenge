package com.exam.spring.repository;

import com.exam.spring.dto.CustomerDTO;
import com.exam.spring.dto.CustomerRequestDTO;
import com.exam.spring.helpers.FilterUtils;
import com.exam.spring.interfaces.ICustomersRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class CustomersRepository implements ICustomersRepository {
    private Map<Integer, CustomerDTO> customers = new HashMap<>();


    @Override
    public CustomerDTO addCustomer(CustomerRequestDTO customerRequestDTO) {
        CustomerDTO newCustomer = new CustomerDTO();
        newCustomer.setDni(customerRequestDTO.getDni());
        newCustomer.setLastname(customerRequestDTO.getLastname());
        newCustomer.setName(customerRequestDTO.getName());
        newCustomer.setProvince(customerRequestDTO.getProvince());
        this.customers.put(newCustomer.getCustomerId(), newCustomer);
        return newCustomer;
    }

    @Override
    public Optional<CustomerDTO> getCustomerByDNI(String dni) {
        return this.customers.values().stream().filter(customerDTO -> customerDTO.getDni().equals(dni)).findFirst();
    }

    @Override
    public List<CustomerDTO> getCustomers() {
        return new ArrayList<>(this.customers.values());
    }

    @Override
    public List<CustomerDTO> getCustomersWithFilter(Map<String, String> filters, List<CustomerDTO> customerList) {
        List<Predicate<CustomerDTO>> allPredicates = new ArrayList<>();

        for (Map.Entry<String, String> filter : filters.entrySet()) {
            switch(filter.getKey())
            {
                case "name":
                    allPredicates.add(FilterUtils.filterCustomersByName(filter.getValue()));
                    break;
                case "province":
                    allPredicates.add(FilterUtils.filterCustomersByProvince(filter.getValue()));
                    break;
            }
        }

        return customerList
            .stream()
            .filter(allPredicates.stream().reduce(x->true, Predicate::and))
            .collect(Collectors.toList());
    }
}