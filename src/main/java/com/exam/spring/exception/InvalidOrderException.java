package com.exam.spring.exception;

import com.exam.spring.dto.ErrorDTO;

public class InvalidOrderException extends SearchEngineException{
    public InvalidOrderException(ErrorDTO errorDTO) {
        super(errorDTO);
    }
}
