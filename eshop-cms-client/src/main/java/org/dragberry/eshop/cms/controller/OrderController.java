package org.dragberry.eshop.cms.controller;

import org.dragberry.eshop.cms.model.OrderCmsModel;
import org.dragberry.eshop.cms.service.OrderCmsService;
import org.dragberry.eshop.common.PageableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public PageableList<OrderCmsModel> getOrders(
	        @RequestParam(required = true) int pageNumber,
	        @RequestParam(required = true) int pageSize) {
		return orderService.getOrders(PageRequest.of(pageNumber - 1, pageSize));
	}

}
