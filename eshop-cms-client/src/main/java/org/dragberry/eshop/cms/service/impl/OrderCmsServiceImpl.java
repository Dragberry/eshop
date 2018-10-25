package org.dragberry.eshop.cms.service.impl;

import java.util.stream.Collectors;

import org.dragberry.eshop.cms.model.OrderCmsModel;
import org.dragberry.eshop.cms.service.OrderCmsService;
import org.dragberry.eshop.common.PageableList;
import org.dragberry.eshop.dal.entity.Order;
import org.dragberry.eshop.dal.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class OrderCmsServiceImpl implements OrderCmsService {

	@Autowired
	private OrderRepository orderRepo;
	
	@Override
	public PageableList<OrderCmsModel> getOrders(PageRequest pageRequest) {
		Page<Order> page = orderRepo.findAll(pageRequest);
		return PageableList.of(page.stream().map(entity -> {
			OrderCmsModel order = new OrderCmsModel();
			order.setId(entity.getEntityKey());
			order.setPhone(entity.getPhone());
			order.setTotalAmount(entity.getTotalAmount());
			order.setFullName(entity.getFullName());
			order.setEmail(entity.getEmail());
			order.setDate(entity.getCreatedDate());
			order.setAddress(entity.getAddress());
			order.setComment(entity.getComment());
			if (entity.getShippingMethod() != null) {
    			order.setShippingMethodId(entity.getShippingMethod().getEntityKey());
    			order.setShippingMethod(entity.getShippingMethod().getName());
			}
			if (entity.getPaymentMethod() != null) {
    			order.setPaymentMethodId(entity.getPaymentMethod().getEntityKey());
    			order.setPaymentMethod(entity.getPaymentMethod().getName());
			}
			order.setStatus(entity.getOrderStatus());
			order.setVersion(entity.getVersion());
			return order;
		}).collect(Collectors.toList()), page.getNumber() + 1, page.getSize(), 10, page.getTotalElements());
	}

}
