package com.exam.spring.helpers;

import com.exam.spring.dto.StatusCodeDTO;
import org.springframework.http.HttpStatus;

public class StatusCode {
    public static StatusCodeDTO getSuccessfulOperationStatusCode() {
        return new StatusCodeDTO("Operation performed successfully", HttpStatus.OK);
    }

    public static StatusCodeDTO getCustomStatusCode(String message, HttpStatus statusCode){
        return new StatusCodeDTO(message, statusCode);
    }
}
