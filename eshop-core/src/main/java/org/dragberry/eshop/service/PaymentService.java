package org.dragberry.eshop.service;

import java.util.List;

import org.dragberry.eshop.model.payment.PaymentMethod;

public interface PaymentService {
	
	List<PaymentMethod> getPaymentMethods();

}
