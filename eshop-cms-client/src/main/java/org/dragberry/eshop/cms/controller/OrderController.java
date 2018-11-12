package org.dragberry.eshop.cms.controller;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.dragberry.eshop.cms.controller.exception.ResourceNotFoundException;
import org.dragberry.eshop.cms.model.OrderDetailsTO;
import org.dragberry.eshop.cms.model.ProductListItemTO;
import org.dragberry.eshop.cms.model.OrderTO;
import org.dragberry.eshop.cms.service.OrderCmsService;
import org.dragberry.eshop.cms.service.ProductCmsService;
import org.dragberry.eshop.common.PageableList;
import org.dragberry.eshop.common.ResultTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
	private HttpServletRequest request;
	
	@Autowired
	private OrderCmsService orderService;
	
	@Autowired
    private ProductCmsService productService;
	
	/**
	 * Get the list of orders
	 * @return
	 */
	@GetMapping("${cms.context}/orders")
	public PageableList<OrderTO> getOrders(
	        @RequestParam(required = true) Integer pageNumber,
	        @RequestParam(required = true) Integer pageSize,
	        HttpServletRequest request) {
		return orderService.getOrders(PageRequest.of(pageNumber - 1, pageSize), request.getParameterMap());
	}
	
	@GetMapping("${cms.context}/orders/{id}")
	public OrderDetailsTO getOrderDetails(@PathVariable Long id) {
	    return orderService.getOrderDetails(id).orElseThrow(ResourceNotFoundException::new);
	}
	
	@PutMapping("${cms.context}/orders/{id}")
	@ResponseBody
    public ResultTO<OrderDetailsTO> updateDetails(@PathVariable Long id, @RequestBody OrderDetailsTO order) {
        return orderService.updateOrder(id, order).orElseThrow(ResourceNotFoundException::new);
    }
	
	@PutMapping("${cms.context}/orders/new")
	@ResponseBody
    public ResultTO<OrderDetailsTO> createDetails(@RequestBody OrderDetailsTO order) {
        return orderService.createOrder(order);
    }

	/**
     * Get the list of product for the given query. It is used to quick product search to add new order item to existing order
     * @return
     */
    @GetMapping("${cms.context}/products/search")
    public PageableList<ProductListItemTO> searchProducts(
            @RequestParam(required = true) Integer pageNumber,
            @RequestParam(required = true) Integer pageSize,
            @RequestParam(required = true) String query) {
        return productService.searchProducts(PageRequest.of(pageNumber - 1, pageSize, Sort.by("entityKey")), request.getParameterMap());
    }
    
    /**
     * Get the available product options for the given article
     * @param pageNumber
     * @param pageSize
     * @param query
     * @return
     */
    @GetMapping("${cms.context}/products/{productArticleId}/options")
    public List<ProductListItemTO> searchProducts(@PathVariable(required = true) Long productArticleId) {
        return productService.getProductOptions(productArticleId).orElseThrow(ResourceNotFoundException::new);
    }
    
    @GetMapping("${cms.context}/orders/{orderId}/download") 
    public void downloadReport(@PathVariable Long orderId, HttpServletResponse resp) throws IOException {
    	resp.setHeader("Content-Disposition", MessageFormat.format("attachment; filename=\"order_{0}.docx\"", orderId.toString()));
    	resp.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    	IOUtils.copy(
    			orderService.generateReport(orderId).orElseThrow(ResourceNotFoundException::new),
    			resp.getOutputStream());
    }
}
