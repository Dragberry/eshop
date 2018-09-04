package org.dragberry.eshop.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.dragberry.eshop.common.IssueTO;
import org.dragberry.eshop.common.Issues;
import org.dragberry.eshop.common.ResultTO;
import org.dragberry.eshop.common.Results;
import org.dragberry.eshop.dal.entity.DeliveryMethod;
import org.dragberry.eshop.dal.entity.Order;
import org.dragberry.eshop.dal.entity.Order.OrderStatus;
import org.dragberry.eshop.dal.entity.OrderItem;
import org.dragberry.eshop.dal.entity.PaymentMethod;
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
	public ResultTO<OrderDetails> createOrder(OrderDetails orderDetails) {
	    List<IssueTO> issues = new ArrayList<>();
	    if (StringUtils.isBlank(orderDetails.getPhone())) {
	        issues.add(Issues.error("msg.error.contactPhoneRequired", "phone"));
	    }
	    if (StringUtils.isNotBlank(orderDetails.getEmail()) && !EmailValidator.getInstance().isValid(orderDetails.getEmail())) {
	        issues.add(Issues.error("msg.error.emailInvalid", "email"));
	    }
	    DeliveryMethod deliveryMethod = null;
	    if (orderDetails.getDeliveryMethod() != null) {
	        deliveryMethod = deliveryMethodRepo.findById(orderDetails.getDeliveryMethod().getId()).orElse(null);
	    }
	    if (deliveryMethod == null) {
	        issues.add(Issues.error("msg.error.deliveryMethodRequired", "deliveryMethod"));
	    }
	    PaymentMethod paymentMethod = null;
        if (orderDetails.getPaymentMethod() != null) {
            paymentMethod = paymentMethodRepo.findById(orderDetails.getPaymentMethod().getId()).orElse(null);
        }
        if (paymentMethod == null) {
            issues.add(Issues.error("msg.error.paymentMethodRequired", "paymentMethod"));
        }
        if (StringUtils.isNotBlank(orderDetails.getFullName()) && GenericValidator.maxLength(orderDetails.getFullName(), 64)) {
            issues.add(Issues.error("msg.error.fullNameIsTooLong", "fullName"));
        }
        if (StringUtils.isNotBlank(orderDetails.getAddress()) && GenericValidator.maxLength(orderDetails.getAddress(), 128)) {
            issues.add(Issues.error("msg.error.addressIsTooLong", "address"));
        }
        if (StringUtils.isNotBlank(orderDetails.getComment()) && GenericValidator.maxLength(orderDetails.getComment(), 128)) {
            issues.add(Issues.error("msg.error.commentIsTooLong", "comment"));
        }
        
        if (issues.isEmpty()) {
    		var order = new Order();
    		order.setOrderStatus(OrderStatus.NEW);
    		order.setPhone(orderDetails.getPhone());
    		order.setFullName(orderDetails.getFullName());
    		order.setAddress(orderDetails.getAddress());
    		order.setEmail(orderDetails.getEmail());
    		order.setItems(orderDetails.getProducts().entrySet().stream().map(cp -> {
    		    var item = new OrderItem();
    			item.setOrder(order);
    			item.setPrice(cp.getValue().getPrice());
    			item.setQuantity(cp.getValue().getQuantity());
    			Optional.ofNullable(cp.getKey().getProductId()).ifPresentOrElse(productId -> {
    			    productRepo.findById(productId).ifPresentOrElse(product -> item.setProduct(product),
    			            () -> issues.add(Issues.error("msg.error.productIsUnknown", cp.getKey())));
    		        }, () -> issues.add(Issues.error("msg.error.productIsNull", cp.getKey())));
    			return item;
    		}).collect(Collectors.toList()));
    		order.setDeliveryMethod(deliveryMethod);
    		order.setPaymentMethod(paymentMethod);
    		var newOrder = orderRepo.save(order);
    		orderDetails.setId(newOrder.getEntityKey());
        }
		return Results.create(orderDetails, issues);
	}

}
