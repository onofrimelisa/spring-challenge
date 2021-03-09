package com.exam.spring.interfaces;

import com.exam.spring.dto.BucketResponseDTO;
import com.exam.spring.dto.ProductDTO;
import com.exam.spring.dto.PurchaseDTO;
import com.exam.spring.exception.InsufficientStockException;
import com.exam.spring.exception.ProductNotFoundException;
import com.exam.spring.exception.ServerErrorException;

import java.util.List;

public interface IProductsRepository {
    List<ProductDTO> loadDatabase() throws ServerErrorException;
    List<ProductDTO> getProducts();
    List<ProductDTO> getProductsWithFilter(String filterKey, String filterValue, List<ProductDTO>motherList);
    List<ProductDTO> orderProducts(List<ProductDTO> productsList, Integer order);
    void checkStock(ProductDTO product, Integer quantity) throws InsufficientStockException, ProductNotFoundException;
    void checkStock(List<PurchaseDTO> purchase) throws InsufficientStockException, ProductNotFoundException;
    ProductDTO getProductById(Integer id) throws ProductNotFoundException;
    Double buyProducts(List<PurchaseDTO> products) throws ProductNotFoundException;
    Double buyProduct(ProductDTO product, Integer quantity);
    void updateStock(ProductDTO product, Integer quantity);
}
