package org.dragberry.eshop.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.dragberry.eshop.dal.entity.PaymentMethod.Status;
import org.dragberry.eshop.dal.repo.PaymentMethodRepository;
import org.dragberry.eshop.model.payment.PaymentMethod;
import org.dragberry.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepo;
    
	@Override
	public List<PaymentMethod> getPaymentMethods() {
		return paymentMethodRepo.findByStatusOrderByOrder(Status.ACTIVE).stream().map(pm -> {
		    return new PaymentMethod(pm.getEntityKey(), pm.getName(), pm.getDescription());
		}).collect(Collectors.toList());
	}

}
