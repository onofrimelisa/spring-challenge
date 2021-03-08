package com.exam.spring.exception;

import com.exam.spring.dto.StatusCodeDTO;

public class InsufficientStockException extends SearchEngineException{
    public InsufficientStockException(StatusCodeDTO statusCodeDTO) {
        super(statusCodeDTO);
    }
}
