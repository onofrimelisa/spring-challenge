package com.exam.spring.exception;

import com.exam.spring.dto.StatusCodeDTO;

public class InsufficientCustomerInformationException extends SearchEngineException{
    public InsufficientCustomerInformationException(StatusCodeDTO statusCodeDTO) {
        super(statusCodeDTO);
    }
}
