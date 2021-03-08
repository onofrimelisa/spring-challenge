package com.exam.spring.interfaces;

import com.exam.spring.dto.ProductsListResponseDTO;

import java.util.Map;

public interface ISearchEngineService{
    ProductsListResponseDTO getProducts();
    ProductsListResponseDTO getProductsWithFilters(Map<String, String> filters, Integer order);
}
