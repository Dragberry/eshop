package org.dragberry.eshop.cms.controller;

import javax.servlet.http.HttpServletRequest;

import org.dragberry.eshop.cms.model.OrderDetailsTO;
import org.dragberry.eshop.cms.model.OrderTO;
import org.dragberry.eshop.cms.service.OrderCmsService;
import org.dragberry.eshop.common.PageableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public PageableList<OrderTO> getOrders(
	        @RequestParam(required = true) int pageNumber,
	        @RequestParam(required = true) int pageSize,
	        HttpServletRequest request) {
		return orderService.getOrders(PageRequest.of(pageNumber - 1, pageSize), request.getParameterMap());
	}
	
	@GetMapping("/details/{id}")
	public OrderDetailsTO getOrderDetails(@PathVariable Long id) {
	    return orderService.getOrderDetails(id).orElseThrow(RuntimeException::new);
	}

}
