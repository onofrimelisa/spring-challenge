package com.exam.spring.service;

import com.exam.spring.dto.ProductDTO;
import com.exam.spring.dto.ProductsListResponseDTO;
import com.exam.spring.interfaces.ISearchEngineRepository;
import com.exam.spring.interfaces.ISearchEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SearchEngineService implements ISearchEngineService {
    @Autowired
    private ISearchEngineRepository searchEngineRepository;

    @Override
    public ProductsListResponseDTO getProducts() {
        List<ProductDTO> products = this.searchEngineRepository.getProducts();
        ProductsListResponseDTO response = new ProductsListResponseDTO();
        response.setArticles(products);
        response.setTotal(products.size());
        return response;
    }

    @Override
    public ProductsListResponseDTO getProductsWithFilters(Map<String, String> filters) {
        List<ProductDTO> productsList = this.getProducts().getArticles();
        ProductsListResponseDTO response = new ProductsListResponseDTO();

        for (Map.Entry<String, String> filter : filters.entrySet()) {
            productsList = this.searchEngineRepository.getProductsWithFilter(filter.getKey(), filter.getValue(), productsList);
        }

        response.setArticles(productsList);
        response.setTotal(productsList.size());

        return response;
    }
}
