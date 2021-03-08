package com.exam.spring.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PurchaseRequestDTO {
    private List<PurchaseDTO> articles;
}
