package com.exam.spring.interfaces;

import com.exam.spring.dto.*;
import com.exam.spring.exception.*;

import java.util.Map;

public interface ISearchEngineService{
    ListResponseDTO<ProductDTO> getProductsWithFilters(Map<String, String> filters, Integer order);
    TicketResponseDTO purchaseRequest(PurchaseRequestDTO purchaseRequestDTO) throws InsufficientStockException, ProductNotFoundException;
    BucketResponseDTO addToBucket(PurchaseDTO purchaseDTO, Integer bucketId) throws InsufficientStockException, ProductNotFoundException;
    BucketResponseDTO purchaseBucket(Integer bucketId) throws ProductNotFoundException, BucketNotFoundException;
    CustomerResponseDTO addCustomer(CustomerRequestDTO customerRequestDTO) throws CustomerAlreadyExistsException, InsufficientCustomerInformationException;
    ListResponseDTO<CustomerDTO> getCustomers(Map<String, String> filters);
}
