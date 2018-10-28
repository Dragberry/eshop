package org.dragberry.eshop.cms.service;

import java.util.Map;

import org.dragberry.eshop.cms.model.OrderCmsModel;
import org.dragberry.eshop.common.PageableList;
import org.springframework.data.domain.PageRequest;

public interface OrderCmsService {

	PageableList<OrderCmsModel> getOrders(PageRequest pageRequest, Map<String, String[]> params);
}
