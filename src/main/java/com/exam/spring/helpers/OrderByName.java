package com.exam.spring.helpers;

import com.exam.spring.dto.ProductDTO;

import java.util.Comparator;

public class OrderByName extends Order {

    public OrderByName(Boolean asc) {
        super(asc);

        Comparator<ProductDTO> comparator;

        if (asc){
            comparator = (prod1, prod2) -> prod1.getName().compareTo(prod2.getName());
        }else{
            comparator = (prod1, prod2) -> prod2.getName().compareTo(prod1.getName());
        }

        this.setComparator(comparator);
    }
}
