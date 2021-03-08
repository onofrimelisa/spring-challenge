package com.exam.spring.helpers;

import com.exam.spring.interfaces.IOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class Order implements IOrder {
    private Boolean asc;
}
