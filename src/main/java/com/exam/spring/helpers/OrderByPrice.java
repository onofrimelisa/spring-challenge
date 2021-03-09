package com.exam.spring.helpers;

import com.exam.spring.dto.ProductDTO;

import java.util.Comparator;

public class OrderByPrice extends Order {
    public OrderByPrice(Boolean asc) {
        super(asc);

        Comparator<ProductDTO> comparator;

        if (asc){
            comparator = (prod1, prod2) -> prod1.getPrice().compareTo(prod2.getPrice());
        }else{
            comparator = (prod1, prod2) -> prod2.getPrice().compareTo(prod1.getPrice());
        }

        this.setComparator(comparator);
    }
}
