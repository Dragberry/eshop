package org.dragberry.eshop.cms.controller;

import java.util.Arrays;
import java.util.List;

import org.dragberry.eshop.dal.entity.PaymentMethod.Status;
import org.dragberry.eshop.model.payment.PaymentMethodTO;
import org.dragberry.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${cms.context}/payment")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/method/list")
    public List<PaymentMethodTO> getPaymentMethodList(@RequestParam(required = true) Status[] status) {
        return paymentService.getPaymentMethods(Arrays.asList(status));
    }
}
