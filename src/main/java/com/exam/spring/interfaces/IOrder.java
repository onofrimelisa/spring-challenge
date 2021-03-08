package com.exam.spring.interfaces;

import com.exam.spring.dto.ProductDTO;

import java.util.List;

public interface IOrder {
    List<ProductDTO> orderList(List<ProductDTO> products);
}
