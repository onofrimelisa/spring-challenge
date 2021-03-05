package com.exam.spring.interfaces;

import com.exam.spring.dto.ProductDTO;
import com.exam.spring.dto.ProductsListResponseDTO;

import java.util.List;
import java.util.Map;

public interface ISearchEngineService{
    ProductsListResponseDTO getProducts();
    ProductsListResponseDTO getProductsWithFilters(Map<String, String> filters, Integer order);
    List<ProductDTO> orderProducts(List<ProductDTO> productsList, Integer order);
}
