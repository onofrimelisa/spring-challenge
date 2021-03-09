package com.exam.spring.interfaces;

import com.exam.spring.dto.*;
import com.exam.spring.exception.BucketNotFoundException;
import com.exam.spring.exception.CustomerAlreadyExistsException;
import com.exam.spring.exception.InsufficientStockException;
import com.exam.spring.exception.ProductNotFoundException;

import java.util.Map;

public interface ISearchEngineService{
    ProductsListResponseDTO getProducts();
    ProductsListResponseDTO getProductsWithFilters(Map<String, String> filters, Integer order);
    TicketDTO purchaseRequest(PurchaseRequestDTO purchaseRequestDTO) throws InsufficientStockException, ProductNotFoundException;
    BucketResponseDTO addToBucket(PurchaseDTO purchaseDTO, Integer bucketId) throws InsufficientStockException, ProductNotFoundException;
    BucketResponseDTO purchaseBucket(Integer bucketId) throws ProductNotFoundException, BucketNotFoundException;
    CustomerDTO addCustomer(CustomerRequestDTO customerRequestDTO) throws CustomerAlreadyExistsException;
}
