package com.exam.spring.exception;

import com.exam.spring.dto.ErrorDTO;

public class ServerErrorException extends SearchEngineException{
    public ServerErrorException(ErrorDTO errorDTO) {
        super(errorDTO);
    }
}
