package org.dragberry.eshop.dal.repo;

import java.util.List;
import java.util.Map;

import org.dragberry.eshop.dal.dto.ProductArticleListItemDTO;
import org.dragberry.eshop.dal.dto.ProductListItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ProductArticleSearchRepository {

	List<ProductListItemDTO> search(String categoryReference, Map<String, String[]> searchParams);

	List<ProductListItemDTO> quickSearch(String query, Map<String, String[]> searchParams);

	Page<ProductArticleListItemDTO> search(PageRequest pageRequest, Map<String, String[]> parameterMap);
}
