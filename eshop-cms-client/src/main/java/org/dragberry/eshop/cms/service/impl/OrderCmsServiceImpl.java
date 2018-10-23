package org.dragberry.eshop.cms.service.impl;

import java.util.stream.Collectors;

import org.dragberry.eshop.cms.model.OrderCmsModel;
import org.dragberry.eshop.cms.service.OrderCmsService;
import org.dragberry.eshop.dal.entity.Order;
import org.dragberry.eshop.dal.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class OrderCmsServiceImpl implements OrderCmsService {

	@Autowired
	private OrderRepository orderRepo;
	
	@Override
	public Page<OrderCmsModel> getOrders(PageRequest pageRequest) {
		Page<Order> result = orderRepo.findAll(pageRequest);
		return new PageImpl<OrderCmsModel>(result.stream().map(entity -> {
			OrderCmsModel order = new OrderCmsModel();
			order.setId(entity.getEntityKey());
			order.setPhone(entity.getPhone());
			order.setFullName(entity.getFullName());
			order.setEmail(entity.getEmail());
			order.setDate(entity.getCreatedDate());
			order.setAddress(entity.getAddress());
			order.setComment(entity.getComment());
			order.setShippingMethodId(entity.getShippingMethod().getEntityKey());
			order.setShippingMethod(entity.getShippingMethod().getName());
			order.setPaymentMethodId(entity.getPaymentMethod().getEntityKey());
			order.setPaymentMethod(entity.getPaymentMethod().getName());
			order.setStatus(entity.getOrderStatus());
			order.setVersion(entity.getVersion());
			return order;
		}).collect(Collectors.toList()), pageRequest, result.getTotalElements());
	}

}
