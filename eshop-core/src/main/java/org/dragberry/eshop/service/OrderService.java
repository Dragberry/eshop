package org.dragberry.eshop.service;

import org.dragberry.eshop.common.ResultTO;
import org.dragberry.eshop.model.cart.OrderDetails;

public interface OrderService {

    ResultTO<OrderDetails> createOrder(OrderDetails orderDetails);
}
