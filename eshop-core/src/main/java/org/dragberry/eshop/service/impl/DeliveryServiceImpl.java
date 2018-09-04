package org.dragberry.eshop.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.dragberry.eshop.dal.entity.DeliveryMethod.Status;
import org.dragberry.eshop.dal.repo.DeliveryMethodRepository;
import org.dragberry.eshop.model.delivery.DeliveryMethod;
import org.dragberry.eshop.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    private DeliveryMethodRepository deliveryMethodRepo;
    
	@Override
	public List<DeliveryMethod> getDeliveryMethods() {
		return deliveryMethodRepo.findByStatusOrderByOrder(Status.ACTIVE).stream().map(dm -> {
		    return new DeliveryMethod(dm.getEntityKey(), dm.getName(), dm.getDescription(), dm.getPrice());
		}).collect(Collectors.toList());
	}

}
