package com.exam.spring.exception;

import com.exam.spring.dto.ErrorDTO;

public class InsuficientStockException extends SearchEngineException{
    public InsuficientStockException(ErrorDTO errorDTO) {
        super(errorDTO);
    }
}
