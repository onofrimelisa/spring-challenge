package com.exam.spring.helpers;

import com.exam.spring.dto.ProductDTO;
import com.exam.spring.interfaces.IOrder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrderAlphabetically implements IOrder {

    @Override
    public List<ProductDTO> orderAsc(List<ProductDTO> products) {
        return products.stream().sorted(Comparator.comparing(ProductDTO::getName)).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> orderDesc(List<ProductDTO> products) {
        return products.stream().sorted(Comparator.comparing(ProductDTO::getName).reversed()).collect(Collectors.toList());
    }
}
