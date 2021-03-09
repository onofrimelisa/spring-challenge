package com.exam.spring.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductsListResponseDTO {
    private List<ProductDTO> articles;
    private Integer total;
    private StatusCodeDTO statusCodeDTO;
}
