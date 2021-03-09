package com.exam.spring.repository;

import com.exam.spring.dto.CustomerDTO;
import com.exam.spring.dto.CustomerRequestDTO;
import com.exam.spring.interfaces.ICustomersRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
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
    public List<CustomerDTO> getCustomersWithFilter(String key, String value, List<CustomerDTO> motherList) {
        switch(key)
        {
            case "name":
                motherList = this.filterByName(value, motherList);
                break;
            case "province":
                motherList = this.filterByProvince(value, motherList);
                break;
        }

        return motherList;
    }

    /* #######################################################################################################

                                            HELPERS

     ######################################################################################################### */

    private List<CustomerDTO> filterByProvince(String province, List<CustomerDTO> listToFilter){
        return listToFilter.stream().filter(customer -> customer.getProvince().equalsIgnoreCase(province)).collect(Collectors.toList());
    }

    private List<CustomerDTO> filterByName(String name, List<CustomerDTO> listToFilter) {
        return listToFilter.stream().filter(customer -> customer.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
    }
}