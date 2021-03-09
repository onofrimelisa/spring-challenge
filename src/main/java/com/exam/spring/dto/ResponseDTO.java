package com.exam.spring.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ResponseDTO {
    private StatusCodeDTO statusCodeDTO;
}
