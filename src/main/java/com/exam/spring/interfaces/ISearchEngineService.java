package com.exam.spring.interfaces;

import com.exam.spring.dto.*;
import com.exam.spring.exception.*;

import java.util.List;
import java.util.Map;

public interface ISearchEngineService{
    ListResponseDTO<ProductDTO> getProducts();
    ListResponseDTO<ProductDTO> getProductsWithFilters(Map<String, String> filters, Integer order);
    TicketDTO purchaseRequest(PurchaseRequestDTO purchaseRequestDTO) throws InsufficientStockException, ProductNotFoundException;
    BucketResponseDTO addToBucket(PurchaseDTO purchaseDTO, Integer bucketId) throws InsufficientStockException, ProductNotFoundException;
    BucketResponseDTO purchaseBucket(Integer bucketId) throws ProductNotFoundException, BucketNotFoundException;
    CustomerDTO addCustomer(CustomerRequestDTO customerRequestDTO) throws CustomerAlreadyExistsException, InsufficientCustomersInformationException;
    ListResponseDTO<CustomerDTO> getCustomers();
}
