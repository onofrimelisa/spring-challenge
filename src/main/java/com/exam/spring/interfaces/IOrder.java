package com.exam.spring.interfaces;

import com.exam.spring.dto.ProductDTO;

import java.util.List;

public interface IOrder {
    List<ProductDTO> orderAsc(List<ProductDTO> products);
    List<ProductDTO> orderDesc(List<ProductDTO> products);
}
