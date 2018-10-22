package org.dragberry.eshop.cms.service;

import java.util.List;

import org.dragberry.eshop.cms.model.OrderCmsModel;

public interface OrderCmsService {

	List<OrderCmsModel> getOrders();
}
