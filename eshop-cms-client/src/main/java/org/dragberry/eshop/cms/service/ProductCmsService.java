package org.dragberry.eshop.cms.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.dragberry.eshop.cms.model.ProductListItemTO;
import org.dragberry.eshop.cms.model.ProductArticleDetailsTO;
import org.dragberry.eshop.cms.model.ProductArticleListItemTO;
import org.dragberry.eshop.cms.model.ProductCategoryTO;
import org.dragberry.eshop.common.PageableList;
import org.dragberry.eshop.common.ResultTO;
import org.springframework.data.domain.PageRequest;

public interface ProductCmsService {

    PageableList<ProductListItemTO> searchProducts(PageRequest pageRequest, Map<String, String[]> searchParams);

    Optional<List<ProductListItemTO>> getProductOptions(Long productArticleId);

    PageableList<ProductArticleListItemTO> getProducts(Integer pageNumber, Integer pageSize, Map<String, String[]> parameterMap);

    List<ProductCategoryTO> getCategoryTree();

	Optional<ProductArticleDetailsTO> getProductArticleDetails(Long productArticleId);

    Optional<ResultTO<ProductArticleDetailsTO>> updateAttributes(Long productArticleId, ProductArticleDetailsTO product);
}
