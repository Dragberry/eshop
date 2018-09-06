package org.dragberry.eshop.model.cart;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartState<T> {

    private int quantity;
    
    private BigDecimal totalProductAmount;
    
    private BigDecimal shippingCost;

    private BigDecimal totalAmount;
    
    private T value;
}
