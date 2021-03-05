package com.exam.spring.controller;

import com.exam.spring.dto.ErrorDTO;
import com.exam.spring.dto.ProductsListResponseDTO;
import com.exam.spring.exception.SearchEngineException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SearchEngineController {

    @GetMapping("/articles")
    public ProductsListResponseDTO getProducts(){
        return null;
    }

    @ExceptionHandler(SearchEngineException.class)
    public ResponseEntity<ErrorDTO> handleSearchEngineException(SearchEngineException searchEngineException) {
        return new ResponseEntity<>(searchEngineException.getErrorDTO(), searchEngineException.getErrorDTO().getStatus());
    }
}
