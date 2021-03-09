package com.exam.spring.helpers;

import com.exam.spring.dto.ProductDTO;
import com.exam.spring.interfaces.IOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public abstract class Order implements IOrder {
    private Boolean asc;
    Comparator<ProductDTO> comparator;

    public Order(Boolean asc) {
        this.asc = asc;
    }

    @Override
    public void orderList(List<ProductDTO> products) {
        products.sort(this.getComparator());
    }
}
