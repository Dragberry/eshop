package org.dragberry.eshop.service;

import java.util.List;

import org.dragberry.eshop.dal.entity.ShippingMethod.Status;
import org.dragberry.eshop.model.shipping.ShippingMethodTO;

public interface ShippingService {
	
	List<ShippingMethodTO> getShippingMethods(List<Status> statuses);

}
