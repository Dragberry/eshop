package org.dragberry.eshop.cms.controller;

import java.util.List;

import org.dragberry.eshop.cms.model.OrderCmsModel;
import org.dragberry.eshop.cms.service.OrderCmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("${cms.context}/orders")
public class OrderController {
	
	@Autowired
	private OrderCmsService orderService;
	
	/**
	 * Get the list of orders
	 * @return
	 */
	@GetMapping("/list")
	public List<OrderCmsModel> getOrders() {
		return orderService.getOrders();
	}

}
