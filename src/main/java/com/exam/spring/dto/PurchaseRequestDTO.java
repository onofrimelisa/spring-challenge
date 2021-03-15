package com.exam.spring.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PurchaseRequestDTO {
    private List<PurchaseDTO> articles;
}
