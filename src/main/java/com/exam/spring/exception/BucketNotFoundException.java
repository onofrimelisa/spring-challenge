package com.exam.spring.exception;

import com.exam.spring.dto.StatusCodeDTO;

public class BucketNotFoundException extends SearchEngineException{
    public BucketNotFoundException(StatusCodeDTO statusCodeDTO) {
        super(statusCodeDTO);
    }
}
