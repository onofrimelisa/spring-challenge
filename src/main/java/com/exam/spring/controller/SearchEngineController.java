package com.exam.spring.controller;

import com.exam.spring.dto.ErrorDTO;
import com.exam.spring.dto.ProductsListResponseDTO;
import com.exam.spring.exception.SearchEngineException;
import com.exam.spring.interfaces.ISearchEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SearchEngineController {
    @Autowired
    private ISearchEngineService searchEngineService;

    @GetMapping("/articles")
    public ProductsListResponseDTO getProducts(){
        return this.searchEngineService.getProducts();
    }

    @ExceptionHandler(SearchEngineException.class)
    public ResponseEntity<ErrorDTO> handleSearchEngineException(SearchEngineException searchEngineException) {
        return new ResponseEntity<>(searchEngineException.getErrorDTO(), searchEngineException.getErrorDTO().getStatus());
    }
}
