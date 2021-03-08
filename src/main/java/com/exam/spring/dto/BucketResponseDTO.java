package com.exam.spring.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BucketResponseDTO {
    private Integer id;
    private List<PurchaseDTO> articles;
    private Double total;
    private StatusCodeDTO statusCodeDTO;
    //test
}
