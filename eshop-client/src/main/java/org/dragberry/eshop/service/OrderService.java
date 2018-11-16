package org.dragberry.eshop.service;

import org.dragberry.eshop.common.ResultTO;
import org.dragberry.eshop.model.cart.OrderDetails;
import org.dragberry.eshop.model.cart.QuickOrderDetails;

public interface OrderService {

    /**
     * Validates and create order. Called from cart
     * @param orderDetails
     * @return
     */
    ResultTO<OrderDetails> createOrder(OrderDetails orderDetails);
    
    /**
     * Validates and create quick order. Called from product single-click order page
     * @param orderDetails
     * @return
     */
    ResultTO<QuickOrderDetails> createQuickOrder(QuickOrderDetails orderDetails);
}
