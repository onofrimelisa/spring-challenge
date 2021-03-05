package com.exam.spring.exception;

import com.exam.spring.dto.ErrorDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class SearchEngineException extends Exception{
    private ErrorDTO errorDTO;

    public SearchEngineException(ErrorDTO errorDTO) {
        super(errorDTO.getMessage());
    }
}
