package com.exam.spring.repository;

import com.exam.spring.dto.CustomerDTO;
import com.exam.spring.dto.CustomerRequestDTO;
import com.exam.spring.interfaces.ICustomersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class CustomersRepositoryTest {
    private ICustomersRepository customersRepository;

    @BeforeEach
    public void setContext(){
        this.customersRepository = new CustomersRepository();
    }

    @Test
    void addCustomer(){
        // Arrange
        CustomerRequestDTO customerRequest = getCustomerRequestDTO();

        // Act
        CustomerDTO result = this.customersRepository.addCustomer(customerRequest);

        // Assert
        Assertions.assertEquals(customerRequest.getName(), result.getName());
        Assertions.assertEquals(customerRequest.getProvince(), result.getProvince());
        Assertions.assertEquals(customerRequest.getLastname(), result.getLastname());
        Assertions.assertEquals(customerRequest.getDni(), result.getDni());
    }

    @Test
    void getCustomerByExistentDNI(){
        // Arrange
        CustomerRequestDTO customerRequest = getCustomerRequestDTO();
        CustomerDTO customer = this.customersRepository.addCustomer(customerRequest);

        // Act
        Optional<CustomerDTO> result = this.customersRepository.getCustomerByDNI("Foo");

        // Assert
        Assertions.assertTrue(result.isPresent());
        CustomerDTO customerResult = result.get();
        Assertions.assertEquals(customer.getName(), customerResult.getName());
        Assertions.assertEquals(customer.getProvince(), customerResult.getProvince());
        Assertions.assertEquals(customer.getLastname(), customerResult.getLastname());
        Assertions.assertEquals(customer.getDni(), customerResult.getDni());
    }

    @Test
    void getCustomerByNonExistentDNI(){
        // Arrange
        CustomerRequestDTO customerRequest = getCustomerRequestDTO();
        this.customersRepository.addCustomer(customerRequest);

        // Act
        Optional<CustomerDTO> result = this.customersRepository.getCustomerByDNI("Bar");

        // Assert
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void getCustomers(){
        // Arrange
        CustomerRequestDTO customerRequest = getCustomerRequestDTO();
        this.customersRepository.addCustomer(customerRequest);

        // Act
        List<CustomerDTO> result = this.customersRepository.getCustomers();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void getCustomersWithoutFilters(){
        // Arrange
        List<CustomerDTO> customersList = getCustomersList();

        // Act
        List<CustomerDTO> result = this.customersRepository.getCustomersWithFilter(new HashMap<>(), customersList);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(customersList, result);
    }

    @Test
    void getCustomersWithNameFilter(){
        // Arrange
        List<CustomerDTO> customersList = getCustomersList();

        // Act
        HashMap<String, String> filters = new HashMap<>();
        filters.put("name", "FOO");
        List<CustomerDTO> result = this.customersRepository.getCustomersWithFilter(filters, customersList);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertNotEquals(customersList, result);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getCustomersWithProvinceFilter(){
        // Arrange
        List<CustomerDTO> customersList = getCustomersList();

        // Act
        HashMap<String, String> filters = new HashMap<>();
        filters.put("province", "FOO");
        List<CustomerDTO> result = this.customersRepository.getCustomersWithFilter(filters, customersList);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertNotEquals(customersList, result);
        Assertions.assertEquals(0, result.size());
    }

    private CustomerRequestDTO getCustomerRequestDTO(){
        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO();
        customerRequestDTO.setDni("Foo");
        customerRequestDTO.setName("Bar");
        customerRequestDTO.setLastname("Baz");
        customerRequestDTO.setProvince("qux");
        return customerRequestDTO;
    }

    private List<CustomerDTO> getCustomersList(){
        CustomerDTO customer = new CustomerDTO();
        customer.setDni("Foo");
        customer.setName("Bar");
        customer.setLastname("Baz");
        customer.setProvince("qux");

        List<CustomerDTO> customersList = new ArrayList<>();
        customersList.add(customer);

        return customersList;
    }

}
