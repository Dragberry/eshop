package org.dragberry.eshop.service;

import org.dragberry.eshop.model.cart.OrderDetails;

public interface OrderService {

	void createOrder(OrderDetails orderDetails);
}
