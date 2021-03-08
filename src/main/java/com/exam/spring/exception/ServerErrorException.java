package com.exam.spring.exception;

import com.exam.spring.dto.StatusCodeDTO;

public class ServerErrorException extends SearchEngineException{
    public ServerErrorException(StatusCodeDTO statusCodeDTO) {
        super(statusCodeDTO);
    }
}
