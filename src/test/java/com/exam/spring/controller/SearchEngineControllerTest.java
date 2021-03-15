package com.exam.spring.controller;

import com.exam.spring.dto.ListResponseDTO;
import com.exam.spring.dto.ProductDTO;
import com.exam.spring.dto.StatusCodeDTO;
import com.exam.spring.interfaces.ISearchEngineService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

public class SearchEngineControllerTest {
    private SearchEngineController searchEngineController;
    @Mock
    private ISearchEngineService searchEngineService;

    @BeforeEach
    public void setContext(){
        // initialize the mocks of this class
        initMocks(this);
        searchEngineController = new SearchEngineController(searchEngineService);
    }

    @Test
    public void getProductsWithoutAnyFilter() {
        // set
        List<ProductDTO> products = getListOfProducts();
        ListResponseDTO<ProductDTO> response = new ListResponseDTO();
        response.setList(products);
        response.setStatusCodeDTO(getSuccessfulOperationStatusCode());
        response.setTotal(products.size());
        Mockito.when(searchEngineService.getProductsWithFilters(new HashMap<>(), 0)).thenReturn(response);

        // act
        ListResponseDTO result = this.searchEngineController.getProductsFiltered(null, null, null, null, null, null, 0);

        //assert
        Assertions.assertIterableEquals(products, result.getList());
        Assertions.assertEquals(result.getStatusCodeDTO().getStatus(), HttpStatus.OK);
    }

    @Test
    public void getProductsFilteredByCategory() {
        // set
        String category = "Herramientas";
        List<ProductDTO> products = getListOfProductsWithASpecificCategory();
        ListResponseDTO<ProductDTO> response = new ListResponseDTO();
        response.setList(products);
        response.setStatusCodeDTO(getSuccessfulOperationStatusCode());
        response.setTotal(products.size());
        HashMap<String, String> filters = new HashMap<>();
        filters.put("category", category);
        Mockito.when(searchEngineService.getProductsWithFilters(filters, 0)).thenReturn(response);

        // act
        ListResponseDTO result = this.searchEngineController.getProductsFiltered(null, category, null, null, null, null, 0);

        //assert
        Assertions.assertIterableEquals(products, result.getList());
        Assertions.assertEquals(2, result.getList().size());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCodeDTO().getStatus());
    }

    private List<ProductDTO> getListOfProducts(){
        List<ProductDTO> products = new ArrayList<>();

        ProductDTO product1 = new ProductDTO();
        product1.setStock(5);
        product1.setBrand("Nike");
        product1.setCategory("Indumentaria");
        product1.setPrestige(5);
        product1.setPrice(500d);
        product1.setFreeShipping(true);
        product1.setName("Remera");
        product1.setId(0);

        ProductDTO product2 = new ProductDTO();
        product2.setStock(5);
        product2.setBrand("Puma");
        product2.setCategory("Indumentaria");
        product2.setPrestige(5);
        product2.setPrice(500d);
        product2.setFreeShipping(true);
        product2.setName("Pantalón");
        product2.setId(1);

        products.add(product1);
        products.add(product2);
        products.addAll(getListOfProductsWithASpecificCategory());

        return products;
    }

    private List<ProductDTO> getListOfProductsWithASpecificCategory(){
        List<ProductDTO> products = new ArrayList<>();

        ProductDTO product3 = new ProductDTO();
        product3.setStock(5);
        product3.setBrand("Hermanos");
        product3.setCategory("Herramientas");
        product3.setPrestige(5);
        product3.setPrice(500d);
        product3.setFreeShipping(true);
        product3.setName("Destornillador");
        product3.setId(2);

        ProductDTO product4 = new ProductDTO();
        product4.setStock(5);
        product4.setBrand("Cetol");
        product4.setCategory("Herramientas");
        product4.setPrestige(5);
        product4.setPrice(500d);
        product4.setFreeShipping(false);
        product4.setName("Cetol");
        product4.setId(3);

        products.add(product3);
        products.add(product4);

        return products;
    }

    private StatusCodeDTO getSuccessfulOperationStatusCode() {
        return new StatusCodeDTO("Operation performed successfully", HttpStatus.OK);
    }
}
