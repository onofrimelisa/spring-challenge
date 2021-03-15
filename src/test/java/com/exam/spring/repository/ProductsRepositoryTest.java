package com.exam.spring.repository;

import com.exam.spring.dto.ProductDTO;
import com.exam.spring.exception.ServerErrorException;
import com.exam.spring.helpers.OrderByName;
import com.exam.spring.interfaces.IProductsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

class ProductsRepositoryTest {
    private IProductsRepository productsRepository;

    @BeforeEach
    public void setContext() throws ServerErrorException {
        initMocks(this);
        this.productsRepository = new ProductsRepository();
    }

    @Test
    void getProducts() {
        // Act
        List<ProductDTO> result = this.productsRepository.getProducts();

        // Assertions
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() > 0);
    }

    @Test
    void orderProductsByNameAsc(){
        // Arrange
        List<ProductDTO> notOrderedList = getListOfProducts();

        // Act
        this.productsRepository.orderProducts(notOrderedList, 0);

        // Assert
        Assertions.assertTrue(notOrderedList.get(0).getName().compareToIgnoreCase(notOrderedList.get(1).getName()) < 0);
    }

    @Test
    void orderProductsByNameDesc(){
        // Arrange
        List<ProductDTO> notOrderedList = getListOfProducts();

        // Act
        this.productsRepository.orderProducts(notOrderedList, 1);

        // Assert
        Assertions.assertTrue(notOrderedList.get(0).getName().compareToIgnoreCase(notOrderedList.get(1).getName()) > 0);
    }

    @Test
    void orderProductsByPriceAsc(){
        // Arrange
        List<ProductDTO> notOrderedList = getListOfProducts();

        // Act
        this.productsRepository.orderProducts(notOrderedList, 2);

        // Assert
        Assertions.assertTrue(notOrderedList.get(0).getPrice().compareTo(notOrderedList.get(1).getPrice()) > 0);
    }

    @Test
    void orderProductsByPriceDesc(){
        // Arrange
        List<ProductDTO> notOrderedList = getListOfProducts();

        // Act
        this.productsRepository.orderProducts(notOrderedList, 3);

        // Assert
        Assertions.assertTrue(notOrderedList.get(0).getPrice().compareTo(notOrderedList.get(1).getPrice()) < 0);
    }

    private List<ProductDTO> getListOfProducts(){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(0);
        productDTO.setPrice(100d);
        productDTO.setName("Foo");
        productDTO.setCategory("Bar");
        productDTO.setBrand("Baz");
        productDTO.setPrestige(0);
        productDTO.setStock(1);
        productDTO.setFreeShipping(true);

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setId(1);
        productDTO2.setPrice(35d);
        productDTO2.setName("Bar");
        productDTO2.setCategory("Baz");
        productDTO2.setBrand("Foo");
        productDTO2.setPrestige(0);
        productDTO2.setStock(2);
        productDTO2.setFreeShipping(false);

        List<ProductDTO> productList = new ArrayList<>();
        productList.add(productDTO);
        productList.add(productDTO2);

        return productList;
    }
}
