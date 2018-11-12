package org.dragberry.eshop.cms.controller;

import java.util.Arrays;
import java.util.List;

import org.dragberry.eshop.dal.entity.ShippingMethod.Status;
import org.dragberry.eshop.model.shipping.ShippingMethodTO;
import org.dragberry.eshop.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${cms.context}/shipping")
public class ShippingController {
    
    @Autowired
    private ShippingService shippingService;

    @GetMapping("/method/list")
    public List<ShippingMethodTO> getShippingMethodList(@RequestParam(required = true) Status[] status) {
        return shippingService.getShippingMethods(Arrays.asList(status));
    }
}
