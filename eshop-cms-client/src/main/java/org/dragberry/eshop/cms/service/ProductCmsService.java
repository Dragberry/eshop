package org.dragberry.eshop.cms.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.dragberry.eshop.cms.model.OrderProductTO;
import org.dragberry.eshop.cms.model.ProductArticleListItemTO;
import org.dragberry.eshop.common.PageableList;
import org.springframework.data.domain.PageRequest;

public interface ProductCmsService {

    PageableList<OrderProductTO> searchProducts(PageRequest pageRequest, Map<String, String[]> searchParams);

    Optional<List<OrderProductTO>> getProductOptions(Long productArticleId);

    PageableList<ProductArticleListItemTO> getProducts(Integer pageNumber, Integer pageSize, Map<String, String[]> parameterMap);
}
