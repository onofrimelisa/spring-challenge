package com.exam.spring.exception;

import com.exam.spring.dto.StatusCodeDTO;

public class CustomerAlreadyExistException extends SearchEngineException{
    public CustomerAlreadyExistException(StatusCodeDTO statusCodeDTO) {
        super(statusCodeDTO);
    }
}
