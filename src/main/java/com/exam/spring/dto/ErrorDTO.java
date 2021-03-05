package com.exam.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDTO {
    private String message;
    private HttpStatus status;
}
