package com.exam.spring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer id;
    private String name;
    private String category;
    private String brand;
    private Double price;
    private Integer stock;
    private Boolean freeShipping;
    private Integer prestige;
}
