package com.exam.spring.interfaces;

import com.exam.spring.dto.ProductDTO;

import java.util.List;

public interface IOrder {
    void orderList(List<ProductDTO> products);
}
