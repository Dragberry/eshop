package org.dragberry.eshop.service;

import java.util.List;

import org.dragberry.eshop.dal.entity.PaymentMethod.Status;
import org.dragberry.eshop.model.payment.PaymentMethodTO;

public interface PaymentService {
	
	List<PaymentMethodTO> getPaymentMethods(List<Status> list);

}
