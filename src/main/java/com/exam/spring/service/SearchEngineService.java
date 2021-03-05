package com.exam.spring.service;

import com.exam.spring.dto.ProductsListResponseDTO;
import com.exam.spring.interfaces.ISearchEngineRepository;
import com.exam.spring.interfaces.ISearchEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchEngineService implements ISearchEngineService {
    @Autowired
    private ISearchEngineRepository searchEngineRepository;

    @Override
    public ProductsListResponseDTO getProducts() {
        ProductsListResponseDTO response = new ProductsListResponseDTO();
        response.setArticles(this.searchEngineRepository.getProducts());
        return response;
    }
}
