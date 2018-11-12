package org.dragberry.eshop.cms.controller;

import javax.servlet.http.HttpServletRequest;

import org.dragberry.eshop.cms.service.ProductCmsService;
import org.dragberry.eshop.common.PageableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatalogController {

    @Autowired
    private ProductCmsService productService;
    
    @GetMapping("${cms.context}/catalog/products")
    public PageableList<?> getProdcutList(
            @RequestParam(required = true) Integer pageNumber,
            @RequestParam(required = true) Integer pageSize,
            HttpServletRequest request) {
        return productService.getProducts(pageNumber - 1, pageSize, request.getParameterMap());
    }
}
