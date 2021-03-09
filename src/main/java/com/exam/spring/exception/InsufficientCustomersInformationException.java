package com.exam.spring.exception;

import com.exam.spring.dto.StatusCodeDTO;

public class InsufficientCustomersInformationException extends SearchEngineException{
    public InsufficientCustomersInformationException(StatusCodeDTO statusCodeDTO) {
        super(statusCodeDTO);
    }
}
