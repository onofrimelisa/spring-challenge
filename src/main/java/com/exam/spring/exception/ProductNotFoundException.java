package com.exam.spring.exception;

import com.exam.spring.dto.ErrorDTO;

public class ProductNotFoundException extends SearchEngineException{
    public ProductNotFoundException(ErrorDTO errorDTO) {
        super(errorDTO);
    }
}
