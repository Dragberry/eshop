package org.dragberry.eshop.service.impl;

import java.util.stream.Collectors;

import org.dragberry.eshop.dal.entity.Order;
import org.dragberry.eshop.dal.entity.Order.OrderStatus;
import org.dragberry.eshop.dal.entity.OrderItem;
import org.dragberry.eshop.dal.repo.DeliveryMethodRepository;
import org.dragberry.eshop.dal.repo.OrderRepository;
import org.dragberry.eshop.dal.repo.PaymentMethodRepository;
import org.dragberry.eshop.dal.repo.ProductRepository;
import org.dragberry.eshop.model.cart.OrderDetails;
import org.dragberry.eshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private DeliveryMethodRepository deliveryMethodRepo;
    
	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private PaymentMethodRepository paymentMethodRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Override
	public void createOrder(OrderDetails orderDetails) {
		Order order = new Order();
		order.setOrderStatus(OrderStatus.NEW);
		order.setPhone(orderDetails.getPhone());
		order.setFullName(orderDetails.getFullName());
		order.setAddress(orderDetails.getAddress());
		order.setEmail(orderDetails.getEmail());
		order.setItems(orderDetails.getProducts().entrySet().stream().map(cp -> {
			OrderItem item = new OrderItem();
			item.setOrder(order);
			item.setPrice(cp.getValue().getPrice());
			item.setQuantity(cp.getValue().getQuantity());
			item.setProduct(productRepo.findById(cp.getKey().getProductId()).get());
			return item;
		}).collect(Collectors.toList()));
		order.setDeliveryMethod(deliveryMethodRepo.getOne(orderDetails.getDeliveryMethod().getId()));
		order.setPaymentMethod(paymentMethodRepo.getOne(orderDetails.getPaymentMethod().getId()));
		orderRepo.save(order);
	}

}
