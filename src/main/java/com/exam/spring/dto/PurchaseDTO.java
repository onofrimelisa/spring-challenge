package com.exam.spring.dto;

import lombok.Getter;

@Getter
public class PurchaseDTO {
    private Integer productId;
    private String name;
    private String brand;
    private Integer quantity;
}
