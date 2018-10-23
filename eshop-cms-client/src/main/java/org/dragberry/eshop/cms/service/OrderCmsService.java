package org.dragberry.eshop.cms.service;

import org.dragberry.eshop.cms.model.OrderCmsModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface OrderCmsService {

	Page<OrderCmsModel> getOrders(PageRequest pageRequest);
}
