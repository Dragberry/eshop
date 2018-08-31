package org.dragberry.eshop.model.cart;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartState<T> {

    private int quantity;
    
    private BigDecimal sum;

    private T change;
}