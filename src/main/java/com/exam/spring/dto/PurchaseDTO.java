package com.exam.spring.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseDTO {
    private Integer productId;
    private String name;
    private String brand;
    private Integer quantity;
}
