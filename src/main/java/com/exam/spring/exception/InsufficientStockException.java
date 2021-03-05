package com.exam.spring.exception;

import com.exam.spring.dto.ErrorDTO;

public class InsufficientStockException extends SearchEngineException{
    public InsufficientStockException(ErrorDTO errorDTO) {
        super(errorDTO);
    }
}
