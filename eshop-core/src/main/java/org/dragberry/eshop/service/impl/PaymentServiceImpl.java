package org.dragberry.eshop.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.dragberry.eshop.dal.entity.PaymentMethod;
import org.dragberry.eshop.dal.entity.PaymentMethod.Status;
import org.dragberry.eshop.dal.repo.PaymentMethodRepository;
import org.dragberry.eshop.model.payment.PaymentMethodTO;
import org.dragberry.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepo;
    
	@Override
	public List<PaymentMethodTO> getPaymentMethods(List<Status> statuses) {
	    List<PaymentMethod> result = statuses.isEmpty() ?
	            paymentMethodRepo.findAllByOrderByOrder() : paymentMethodRepo.findByStatusOrdered(statuses);
        return result.stream().map(pm -> new PaymentMethodTO(pm.getEntityKey(), pm.getName(), pm.getDescription())).collect(Collectors.toList());
	}

}
