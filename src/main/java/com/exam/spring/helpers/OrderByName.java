package com.exam.spring.helpers;

import com.exam.spring.dto.ProductDTO;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrderByName extends Order {

    public OrderByName(Boolean asc) {
        super(asc);
    }

    @Override
    public List<ProductDTO> orderList(List<ProductDTO> products) {
        if (this.getAsc()) return products.stream().sorted(Comparator.comparing(ProductDTO::getName)).collect(Collectors.toList());

        return products.stream().sorted(Comparator.comparing(ProductDTO::getName).reversed()).collect(Collectors.toList());
    }
}
