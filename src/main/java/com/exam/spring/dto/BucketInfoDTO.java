package com.exam.spring.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BucketInfoDTO {
    private List<ProductDTO> articles;
    private Double total;
}
