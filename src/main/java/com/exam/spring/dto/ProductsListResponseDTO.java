package com.exam.spring.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductsListResponseDTO {
    private List<ProductDTO> articles;
    private Integer total;

    public ProductsListResponseDTO() {
        this.articles = new ArrayList<>();
    }
}
