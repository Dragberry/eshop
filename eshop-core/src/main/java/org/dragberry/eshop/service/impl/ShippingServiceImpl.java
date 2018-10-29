package org.dragberry.eshop.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.dragberry.eshop.dal.entity.ShippingMethod;
import org.dragberry.eshop.dal.entity.ShippingMethod.Status;
import org.dragberry.eshop.dal.repo.ShippingMethodRepository;
import org.dragberry.eshop.model.shipping.ShippingMethodTO;
import org.dragberry.eshop.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingMethodRepository shippingMethodRepo;
    
	@Override
	public List<ShippingMethodTO> getShippingMethods(List<Status> statuses) {
	    List<ShippingMethod> result = statuses.isEmpty() ?
	            shippingMethodRepo.findAllByOrderByOrder() : shippingMethodRepo.findByStatusOrdered(statuses);
		return result.stream().map(sm -> new ShippingMethodTO(
		        sm.getEntityKey(),
		        sm.getName(),
		        sm.getDescription(),
		        sm.getCost())).collect(Collectors.toList());
	}

}
