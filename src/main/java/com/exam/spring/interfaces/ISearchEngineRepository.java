package com.exam.spring.interfaces;

import com.exam.spring.dto.ProductDTO;
import com.exam.spring.dto.PurchaseDTO;
import com.exam.spring.exception.InsufficientStockException;
import com.exam.spring.exception.ProductNotFoundException;
import com.exam.spring.exception.ServerErrorException;

import java.util.List;

public interface ISearchEngineRepository {
    List<ProductDTO> loadDatabase() throws ServerErrorException;
    List<ProductDTO> getProducts();
    List<ProductDTO> getProductsWithFilter(String filterKey, String filterValue, List<ProductDTO>motherList, Integer order);
    List<ProductDTO> filterByCategory(String category, List<ProductDTO> listToFilter);
    List<ProductDTO> filterByName(String name, List<ProductDTO> listToFilter);
    List<ProductDTO> filterByBrand(String brand, List<ProductDTO> listToFilter);
    List<ProductDTO> filterByPrice(Double price, List<ProductDTO> listToFilter);
    List<ProductDTO> filterByFreeShipping(Boolean freeShipping, List<ProductDTO> listToFilter);
    List<ProductDTO> filterByPrestige(Integer prestige, List<ProductDTO> listToFilter);
    List<ProductDTO> orderProducts(List<ProductDTO> productsList, Integer order);
    void checkStock(List<PurchaseDTO> purchase) throws InsufficientStockException, ProductNotFoundException;
    ProductDTO getProductById(Integer id) throws ProductNotFoundException;
    Double buyProducts(List<PurchaseDTO> products) throws ProductNotFoundException;
    void updateStock(ProductDTO product, Integer quantity);
}
