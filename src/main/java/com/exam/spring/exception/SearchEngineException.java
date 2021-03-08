package com.exam.spring.exception;

import com.exam.spring.dto.StatusCodeDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class SearchEngineException extends Exception{
    private StatusCodeDTO statusCodeDTO;

    public SearchEngineException(StatusCodeDTO statusCodeDTO) {
        super(statusCodeDTO.getMessage());
        this.statusCodeDTO = statusCodeDTO;
    }
}
