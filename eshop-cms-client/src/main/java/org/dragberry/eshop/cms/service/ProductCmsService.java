package org.dragberry.eshop.cms.service;

import org.dragberry.eshop.cms.model.OrderProductTO;
import org.dragberry.eshop.common.PageableList;
import org.springframework.data.domain.PageRequest;

public interface ProductCmsService {

    PageableList<OrderProductTO> searchProducts(PageRequest pageRequest, String query);
}
