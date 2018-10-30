package org.dragberry.eshop.cms.controller;

import javax.servlet.http.HttpServletRequest;

import org.dragberry.eshop.cms.model.OrderDetailsTO;
import org.dragberry.eshop.cms.model.OrderTO;
import org.dragberry.eshop.cms.service.OrderCmsService;
import org.dragberry.eshop.common.PageableList;
import org.dragberry.eshop.common.ResultTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrderController {
	
	@Autowired
	private OrderCmsService orderService;
	
	/**
	 * Get the list of orders
	 * @return
	 */
	@GetMapping("${cms.context}/orders")
	public PageableList<OrderTO> getOrders(
	        @RequestParam(required = true) int pageNumber,
	        @RequestParam(required = true) int pageSize,
	        HttpServletRequest request) {
		return orderService.getOrders(PageRequest.of(pageNumber - 1, pageSize), request.getParameterMap());
	}
	
	@GetMapping("${cms.context}/orders/{id}")
	public OrderDetailsTO getOrderDetails(@PathVariable Long id) {
	    return orderService.getOrderDetails(id).orElseThrow(RuntimeException::new);
	}
	
	@PutMapping("${cms.context}/orders/{id}")
	@ResponseBody
    public ResultTO<OrderDetailsTO> updateDetails(@PathVariable Long id, @RequestBody OrderDetailsTO order) {
        return orderService.updateOrder(id, order).orElseThrow(RuntimeException::new);
    }

}
