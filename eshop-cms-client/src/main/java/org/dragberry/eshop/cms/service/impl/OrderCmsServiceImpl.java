package org.dragberry.eshop.cms.service.impl;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dragberry.eshop.cms.model.OrderDetailsTO;
import org.dragberry.eshop.cms.model.OrderTO;
import org.dragberry.eshop.cms.service.OrderCmsService;
import org.dragberry.eshop.common.PageableList;
import org.dragberry.eshop.dal.dto.OrderDTO;
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
	public PageableList<OrderTO> getOrders(PageRequest pageRequest, Map<String, String[]> params) {
		Page<OrderDTO> page = orderRepo.search(pageRequest, params);
		return PageableList.of(page.stream().map(entity -> {
			OrderTO order = new OrderTO();
			order.setId(entity.getId());
			order.setPhone(entity.getPhone());
			order.setTotalAmount(entity.getTotalAmount());
			order.setFullName(entity.getFullName());
			order.setEmail(entity.getEmail());
			order.setDate(entity.getDate());
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
		}).collect(Collectors.toList()), page.getNumber() + 1, page.getSize(), 10, page.getTotalElements());
	}
	
	@Override
	public Optional<OrderDetailsTO> getOrderDetails(Long id) {
	    return orderRepo.findById(id).map(entity -> {
	        OrderDetailsTO order = new OrderDetailsTO();
	        order.setId(entity.getEntityKey());
            order.setPhone(entity.getPhone());
            order.setTotalAmount(entity.getTotalAmount());
            order.setFullName(entity.getFullName());
            order.setEmail(entity.getEmail());
            order.setDate(entity.getCreatedDate());
            order.setAddress(entity.getAddress());
            order.setComment(entity.getComment());
            order.setPaid(entity.getPaid());
            order.setStatus(entity.getOrderStatus());
            order.setVersion(entity.getVersion());
	        return order;
	    });
	}

}
