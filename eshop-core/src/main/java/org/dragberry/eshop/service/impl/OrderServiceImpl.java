package org.dragberry.eshop.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.dragberry.eshop.dal.entity.ShippingMethod;
import org.dragberry.eshop.dal.entity.Order;
import org.dragberry.eshop.dal.entity.Order.OrderStatus;
import org.dragberry.eshop.dal.entity.OrderItem;
import org.dragberry.eshop.dal.entity.PaymentMethod;
import org.dragberry.eshop.dal.entity.Product;
import org.dragberry.eshop.dal.repo.ShippingMethodRepository;
import org.dragberry.eshop.dal.repo.OrderRepository;
import org.dragberry.eshop.dal.repo.PaymentMethodRepository;
import org.dragberry.eshop.dal.repo.ProductRepository;
import org.dragberry.eshop.model.cart.OrderDetails;
import org.dragberry.eshop.model.cart.QuickOrderDetails;
import org.dragberry.eshop.service.OrderService;
import org.dragberry.eshop.utils.ProductFullTitleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ShippingMethodRepository shippingMethodRepo;
    
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
	    } else if (!GenericValidator.maxLength(orderDetails.getPhone(), 20)) {
	    	issues.add(Issues.error("msg.error.contactPhoneIsTooLong", "phone"));
	    }
	    if (StringUtils.isNotBlank(orderDetails.getEmail()) 
	    		&& !GenericValidator.maxLength(orderDetails.getEmail(), 128)
	    		&& !EmailValidator.getInstance().isValid(orderDetails.getEmail())) {
	        issues.add(Issues.error("msg.error.common.email.invalid", "email"));
	    }
	    ShippingMethod shippingMethod = null;
	    if (orderDetails.getShippingMethod() != null) {
	        shippingMethod = shippingMethodRepo.findById(orderDetails.getShippingMethod().getId()).orElse(null);
	    }
	    if (shippingMethod == null) {
	        issues.add(Issues.error("msg.error.deliveryMethodRequired", "deliveryMethod"));
	    }
	    PaymentMethod paymentMethod = null;
        if (orderDetails.getPaymentMethod() != null) {
            paymentMethod = paymentMethodRepo.findById(orderDetails.getPaymentMethod().getId()).orElse(null);
        }
        if (paymentMethod == null) {
            issues.add(Issues.error("msg.error.paymentMethodRequired", "paymentMethod"));
        }
        if (StringUtils.isNotBlank(orderDetails.getFullName()) && !GenericValidator.maxLength(orderDetails.getFullName(), 64)) {
            issues.add(Issues.error("msg.error.fullNameIsTooLong", "fullName"));
        }
        if (StringUtils.isNotBlank(orderDetails.getAddress()) && !GenericValidator.maxLength(orderDetails.getAddress(), 128)) {
            issues.add(Issues.error("msg.error.addressIsTooLong", "address"));
        }
        if (StringUtils.isNotBlank(orderDetails.getComment()) && !GenericValidator.maxLength(orderDetails.getComment(), 128)) {
            issues.add(Issues.error("msg.error.commentIsTooLong", "comment"));
        }
        
        if (issues.isEmpty()) {
    		Order order = new Order();
    		order.setOrderStatus(OrderStatus.NEW);
    		order.setPaid(Boolean.FALSE);
    		order.setTotalProductAmount(orderDetails.getTotalProductAmount());
    		order.setShippingCost(orderDetails.getShippingCost());
    		order.setTotalAmount(orderDetails.getTotalAmount());
    		order.setPhone(orderDetails.getPhone());
    		order.setFullName(orderDetails.getFullName());
    		order.setAddress(orderDetails.getAddress());
    		order.setEmail(orderDetails.getEmail());
    		order.setComment(orderDetails.getComment());
    		order.setItems(orderDetails.getProducts().entrySet().stream().map(cp -> {
    		    OrderItem item = new OrderItem();
    			item.setOrder(order);
    			item.setPrice(cp.getValue().getPrice());
    			item.setQuantity(cp.getValue().getQuantity());
    			item.setTotalAmount(cp.getValue().getTotalAmount());
    			Long productId = cp.getKey().getProductId();
    			if (productId != null) {
    				Optional<Product> product = productRepo.findById(productId);
    				if (product.isPresent()) {
    					item.setProduct(product.get());
    				} else {
    					issues.add(Issues.error("msg.error.productIsUnknown", cp.getKey()));
    				}
    			} else {
    				issues.add(Issues.error("msg.error.productIsNull", cp.getKey()));
    			}
    			return item;
    		}).collect(Collectors.toList()));
    		order.setShippingMethod(shippingMethod);
    		order.setPaymentMethod(paymentMethod);
    		Order newOrder = orderRepo.save(order);
    		orderDetails.setId(newOrder.getEntityKey());
        }
		return Results.create(orderDetails, issues);
	}

	@Override
	public ResultTO<QuickOrderDetails> createQuickOrder(QuickOrderDetails orderDetails) {
	    List<IssueTO> issues = new ArrayList<>();
	    if (StringUtils.isBlank(orderDetails.getPhone())) {
            issues.add(Issues.error("msg.error.contactPhoneRequired", "phone"));
        } else if (!GenericValidator.maxLength(orderDetails.getPhone(), 20)) {
            issues.add(Issues.error("msg.error.contactPhoneIsTooLong", "phone"));
        }
	    if (StringUtils.isNotBlank(orderDetails.getFullName()) && !GenericValidator.maxLength(orderDetails.getFullName(), 64)) {
            issues.add(Issues.error("msg.error.fullNameIsTooLong", "fullName"));
        }
	    if (StringUtils.isNotBlank(orderDetails.getAddress()) && !GenericValidator.maxLength(orderDetails.getAddress(), 128)) {
            issues.add(Issues.error("msg.error.addressIsTooLong", "address"));
        }
	    Optional<Product> product = null;
	    if (orderDetails.getProductId() != null) {
            product = productRepo.findById(orderDetails.getProductId());
            if (!product.isPresent()) {
                issues.add(Issues.error("msg.error.productIsUnknown", "productId"));
            }
        } else {
            issues.add(Issues.error("msg.error.productIsNull", "productId"));
        }
	    if (issues.isEmpty() && product != null) {
	        Order order = new Order();
            order.setOrderStatus(OrderStatus.NEW);
            order.setPaid(Boolean.FALSE);
            order.setPhone(orderDetails.getPhone());
            order.setFullName(orderDetails.getFullName());
            order.setAddress(orderDetails.getAddress());
            OrderItem item = new OrderItem();
            item.setOrder(order);
            Product prod = product.get();
            item.setProduct(prod);
            BigDecimal price = prod.getActualPrice() != null ? prod.getActualPrice() : prod.getPrice();
            item.setPrice(price);
            item.setQuantity(1);
            item.setTotalAmount(price);
            order.setItems(Arrays.asList(item));
            order.setTotalProductAmount(price);
            order.setTotalAmount(price);
            order = orderRepo.save(order);
            orderDetails.setId(order.getEntityKey());
            orderDetails.setProductArticle(prod.getProductArticle().getArticle());
            orderDetails.setProductPrice(prod.getActualPrice());
            orderDetails.setProductFullTitle(ProductFullTitleBuilder.buildFullTitle(prod));
	    }
	    return Results.create(orderDetails, issues);
	}
}
