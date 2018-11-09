package org.dragberry.eshop.cms.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.dragberry.eshop.cms.mapper.OrderProductMapper;
import org.dragberry.eshop.cms.model.OrderDetailsTO;
import org.dragberry.eshop.cms.model.OrderItemTO;
import org.dragberry.eshop.cms.model.OrderTO;
import org.dragberry.eshop.cms.service.OrderCmsService;
import org.dragberry.eshop.common.IssueTO;
import org.dragberry.eshop.common.Issues;
import org.dragberry.eshop.common.PageableList;
import org.dragberry.eshop.common.ResultTO;
import org.dragberry.eshop.common.Results;
import org.dragberry.eshop.dal.dto.OrderDTO;
import org.dragberry.eshop.dal.entity.Order;
import org.dragberry.eshop.dal.entity.OrderItem;
import org.dragberry.eshop.dal.repo.OrderRepository;
import org.dragberry.eshop.dal.repo.PaymentMethodRepository;
import org.dragberry.eshop.dal.repo.ProductRepository;
import org.dragberry.eshop.dal.repo.ShippingMethodRepository;
import org.dragberry.eshop.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class OrderCmsServiceImpl implements OrderCmsService {

    private static final class ErrorCodes {
        public static final String PAYMENT_METHOD_INVALID = "orders.validation.paymentMethodInvalid";
        public static final String SHIPPING_METHOD_INVALID = "orders.validation.shippingMethodInvalid";
        public static final String NO_ITEMS = "orders.validation.noItems";
        public static final String PRODUCT_INVALID = "orders.validation.productInvalid";
    }
    
	@Autowired
    private ResourceLoader resourceLoader;
	
	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
    private ProductRepository productRepo;
	
	@Autowired
    private PaymentMethodRepository paymentMethodRepo;
	
	@Autowired
    private ShippingMethodRepository shippingMethodRepo;
	
	@Autowired
	private ImageService imageService;
	
	@Override
	public PageableList<OrderTO> getOrders(PageRequest pageRequest, Map<String, String[]> params) {
		Page<OrderDTO> page = orderRepo.search(pageRequest, params);
		return PageableList.of(page.stream().map(entity -> {
			OrderTO order = new OrderTO();
			order.setId(entity.getId());
			order.setDate(entity.getOrderDate());
			order.setPhone(entity.getPhone());
			order.setTotalAmount(entity.getTotalAmount());
			order.setFullName(entity.getFullName());
			order.setEmail(entity.getEmail());
			order.setAddress(entity.getAddress());
			order.setComment(entity.getComment());
			order.setPaid(entity.getPaid());
			order.setShippingMethodId(entity.getShippingMethodId());
			order.setShippingMethod(entity.getShippingMethod());
			order.setPaymentMethodId(entity.getPaymentMethodId());
			order.setPaymentMethod(entity.getPaymentMethod());
			order.setStatus(entity.getStatus());
			order.setVersion(entity.getVersion());
			return order;
		}).collect(Collectors.toList()), page.getNumber() + 1, page.getSize(), page.getTotalPages(), page.getTotalElements());
	}
	
	@Override
	public Optional<OrderDetailsTO> getOrderDetails(Long id) {
	    return orderRepo.findById(id).map(this::mapDetails);
	}

	@Override
	public Optional<ResultTO<OrderDetailsTO>> updateOrder(Long orderId, OrderDetailsTO order) {
	    List<IssueTO> issues = new ArrayList<>();
	    return orderRepo.findById(orderId).map(entity -> {
	        entity.setVersion(order.getVersion());
	        entity.setPhone(order.getPhone());
	        entity.setFullName(order.getFullName());
	        entity.setAddress(order.getAddress());
	        entity.setEmail(order.getEmail());
	        entity.setComment(order.getComment());
	        entity.setCustomerComment(order.getCustomerComment());
	        entity.setShopComment(order.getShopComment());
	        entity.setDeliveryDateFrom(order.getDeliveryDateFrom());
	        entity.setDeliveryDateTo(order.getDeliveryDateTo());
	        
	        Optional.ofNullable(order.getPaymentMethodId())
	        .flatMap(id -> {
	        	return paymentMethodRepo.findById(order.getPaymentMethodId());
	        }).ifPresentOrElse(entity::setPaymentMethod,
	                () -> issues.add(Issues.error(ErrorCodes.PAYMENT_METHOD_INVALID)));

	        Optional.ofNullable(order.getShippingMethodId())
	        .flatMap(Id -> {
	        	return shippingMethodRepo.findById(order.getShippingMethodId());
	        }).ifPresentOrElse(entity::setShippingMethod,
		                () -> issues.add(Issues.error(ErrorCodes.SHIPPING_METHOD_INVALID)));
		
	        entity.setShippingCost(order.getShippingCost());
	        entity.setTotalProductAmount(order.getTotalProductAmount());
	        entity.setTotalAmount(order.getTotalAmount());
	        entity.setPaid(order.getPaid());
	        entity.setOrderStatus(order.getStatus());
	        
	        if (CollectionUtils.isNotEmpty(order.getItems())) {
		        List<OrderItem> items = order.getItems().stream().map(item -> {
		            return entity.getItems().stream()
		                    .filter(itemEntity -> itemEntity.getEntityKey().equals(item.getId())).findFirst()
		                    .map(itemEntity -> {
	        	                itemEntity.setPrice(item.getPrice());
	        	                itemEntity.setQuantity(item.getQuantity());
	        	                itemEntity.setTotalAmount(item.getTotalAmount());
	        	                itemEntity.setVersion(item.getVersion());
	        	                if (!itemEntity.getProduct().getEntityKey().equals(item.getProduct().getProductId())) {
	            	                productRepo.findById(item.getProduct().getProductId())
	                                    .ifPresentOrElse(itemEntity::setProduct,
	                                        () -> issues.add(Issues.error(ErrorCodes.PRODUCT_INVALID)));
	        	                }
	        	                return itemEntity;
	        	            })
	        	            .orElseGet(() -> {
	        	                OrderItem newItemEntity = new OrderItem();
	        	                newItemEntity.setOrder(entity);
	        	                newItemEntity.setPrice(item.getPrice());
	        	                newItemEntity.setQuantity(item.getQuantity());
	        	                newItemEntity.setTotalAmount(item.getTotalAmount());
	        	                productRepo.findById(item.getProduct().getProductId())
	        	                    .ifPresentOrElse(newItemEntity::setProduct,
	        	                            () -> issues.add(Issues.error(ErrorCodes.PRODUCT_INVALID)));
	        	                return newItemEntity;
	    	                });
		        }).collect(Collectors.toList());
		        entity.getItems().clear();
		        entity.getItems().addAll(items);
	        } else {
	        	issues.add(Issues.error(ErrorCodes.NO_ITEMS));
	        }
	        
	        return issues.isEmpty() ? orderRepo.save(entity) : entity;
	    }).map(entity -> {
            return Results.create(mapDetails(entity), issues);
	    });
	}
	
	@Override
	public ResultTO<OrderDetailsTO> createOrder(OrderDetailsTO order) {
		List<IssueTO> issues = new ArrayList<>();
		Order entity = new Order();
		entity.setOrderDate(LocalDateTime.now());
		
		entity.setPhone(order.getPhone());
        entity.setFullName(order.getFullName());
        entity.setAddress(order.getAddress());
        entity.setEmail(order.getEmail());
        entity.setComment(order.getComment());
        entity.setCustomerComment(order.getCustomerComment());
        entity.setShopComment(order.getShopComment());
        entity.setDeliveryDateFrom(order.getDeliveryDateFrom());
        entity.setDeliveryDateTo(order.getDeliveryDateTo());
        
        Optional.ofNullable(order.getPaymentMethodId())
        .flatMap(id -> {
        	return paymentMethodRepo.findById(order.getPaymentMethodId());
        }).ifPresentOrElse(entity::setPaymentMethod,
                () -> issues.add(Issues.error(ErrorCodes.PAYMENT_METHOD_INVALID)));

        Optional.ofNullable(order.getShippingMethodId())
        .flatMap(Id -> {
        	return shippingMethodRepo.findById(order.getShippingMethodId());
        }).ifPresentOrElse(entity::setShippingMethod,
	                () -> issues.add(Issues.error(ErrorCodes.SHIPPING_METHOD_INVALID)));
	
	    entity.setShippingCost(order.getShippingCost());
	    entity.setTotalProductAmount(order.getTotalProductAmount());
	    entity.setTotalAmount(order.getTotalAmount());
	    entity.setPaid(order.getPaid());
	    entity.setOrderStatus(order.getStatus());
	  
	    if (CollectionUtils.isNotEmpty(order.getItems())) {
	    	entity.setItems(order.getItems().stream().map(item -> {
		    	OrderItem newItemEntity = new OrderItem();
	            newItemEntity.setOrder(entity);
	            newItemEntity.setPrice(item.getPrice());
	            newItemEntity.setQuantity(item.getQuantity());
	            newItemEntity.setTotalAmount(item.getTotalAmount());
	            productRepo.findById(item.getProduct().getProductId())
	                .ifPresentOrElse(newItemEntity::setProduct,
	                        () -> issues.add(Issues.error(ErrorCodes.PRODUCT_INVALID)));
	            return newItemEntity;
		    }).collect(Collectors.toList()));
	    } else {
	    	issues.add(Issues.error(ErrorCodes.NO_ITEMS));
	    }
	    
	    
		return Results.create(mapDetails(issues.isEmpty() ? orderRepo.save(entity) : entity), issues);
	}
	
	private OrderDetailsTO mapDetails(Order entity) {
        OrderDetailsTO order = new OrderDetailsTO();
        order.setId(entity.getEntityKey());
        order.setOrderDate(entity.getOrderDate());
        order.setVersion(entity.getVersion());
        
        order.setPhone(entity.getPhone());
        order.setFullName(entity.getFullName());
        order.setAddress(entity.getAddress());
        order.setEmail(entity.getEmail());
        order.setComment(entity.getComment());
        order.setCustomerComment(entity.getCustomerComment());
        order.setShopComment(entity.getShopComment());
        order.setDeliveryDateFrom(entity.getDeliveryDateFrom());
        order.setDeliveryDateTo(entity.getDeliveryDateTo());
        
        order.setPaymentMethodId(entity.getPaymentMethod() != null ? entity.getPaymentMethod().getEntityKey() : null);
        order.setShippingMethodId(entity.getShippingMethod() != null ? entity.getShippingMethod().getEntityKey() : null);
        order.setShippingCost(entity.getShippingCost());
        order.setTotalProductAmount(entity.getTotalProductAmount());
        order.setTotalAmount(entity.getTotalAmount());
        order.setPaid(entity.getPaid());
        order.setStatus(entity.getOrderStatus());
        
        order.setItems(entity.getItems().stream().map(item -> {
            OrderItemTO itemTO = new OrderItemTO();
            itemTO.setId(item.getEntityKey());
            itemTO.setPrice(item.getPrice());
            itemTO.setQuantity(item.getQuantity());
            itemTO.setTotalAmount(item.getTotalAmount());
            itemTO.setVersion(item.getVersion());
            itemTO.setProduct(OrderProductMapper.map(item.getProduct(), imageService::findMainImage));
            return itemTO;
        }).collect(Collectors.toList())); 
        return order;
    }
	
	@Override
	public Optional<InputStream> generateReport(Long orderId) {
		return orderRepo.findById(orderId).map(order -> {
			try {
				return resourceLoader.getResource("classpath:data/order.docx").getInputStream();
			} catch (IOException ioe) {
				log.error("An error has occured while generating report for order=" + orderId);
				throw new RuntimeException(ioe);
			}
		});
	}
}
