package com.exam.spring.interfaces;

import com.exam.spring.dto.*;
import com.exam.spring.exception.InsufficientStockException;
import com.exam.spring.exception.ProductNotFoundException;

import java.util.Map;

public interface ISearchEngineService{
    ProductsListResponseDTO getProducts();
    ProductsListResponseDTO getProductsWithFilters(Map<String, String> filters, Integer order);
    TicketDTO purchaseRequest(PurchaseRequestDTO purchaseRequestDTO) throws InsufficientStockException, ProductNotFoundException;
    BucketResponseDTO addToBucket(Integer productId, Integer bucketId) throws InsufficientStockException, ProductNotFoundException;
}
