package com.exam.spring.exception;

import com.exam.spring.dto.StatusCodeDTO;

public class CustomerAlreadyExistsException extends SearchEngineException{
    public CustomerAlreadyExistsException(StatusCodeDTO statusCodeDTO) {
        super(statusCodeDTO);
    }
}
