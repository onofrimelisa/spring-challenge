package com.exam.spring.interfaces;

import com.exam.spring.dto.ProductDTO;
import com.exam.spring.exception.ServerErrorException;

import java.util.List;

public interface ISearchEngineRepository {
    List<ProductDTO> loadDatabase() throws ServerErrorException;
    List<ProductDTO> getProducts();
    List<ProductDTO> getProductsWithFilter(String filterKey, String filterValue, List<ProductDTO>motherList);
    List<ProductDTO> filterByCategory(String category, List<ProductDTO> listToFilter);
    List<ProductDTO> filterByName(String name, List<ProductDTO> listToFilter);
    List<ProductDTO> filterByBrand(String brand, List<ProductDTO> listToFilter);
    List<ProductDTO> filterByPrice(Double price, List<ProductDTO> listToFilter);
    List<ProductDTO> filterByFreeShipping(Boolean freeShipping, List<ProductDTO> listToFilter);
    List<ProductDTO> filterByPrestige(Integer prestige, List<ProductDTO> listToFilter);
}
