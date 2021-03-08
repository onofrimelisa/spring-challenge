package com.exam.spring.exception;

import com.exam.spring.dto.StatusCodeDTO;

public class ProductNotFoundException extends SearchEngineException{
    public ProductNotFoundException(StatusCodeDTO statusCodeDTO) {
        super(statusCodeDTO);
    }
}
