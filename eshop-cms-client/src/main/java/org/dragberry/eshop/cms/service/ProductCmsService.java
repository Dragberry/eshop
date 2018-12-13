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
import org.dragberry.eshop.dal.entity.ProductAttribute;
import org.springframework.data.domain.PageRequest;

public interface ProductCmsService {

    PageableList<ProductListItemTO> searchProducts(PageRequest pageRequest, Map<String, String[]> searchParams);

    Optional<List<ProductListItemTO>> getProductOptions(Long productArticleId);

    PageableList<ProductArticleListItemTO> getProducts(Integer pageNumber, Integer pageSize, Map<String, String[]> parameterMap);

    List<ProductCategoryTO> getCategoryTree();

	Optional<ProductArticleDetailsTO> getProductArticleDetails(Long productArticleId);

    Optional<ResultTO<ProductArticleDetailsTO>> updateAttributes(Long productArticleId, ProductArticleDetailsTO product);

    /**
     * Search for available groups for product attributes
     * @param query to search
     * @return list of available groups
     */
	List<String> findGroupsForAttributes(String group);

	/**
     * Search for available name for the given product attribute type
     * @param type of attribute
     * @param query to search
     * @return list of available names
	 * @throws IllegalArgumentException if an invalid type is passed
	 */
	List<String> findNamesForAttributes(String type, String name);

	/**
     * Search for available attribute values for the given product attribute type.
     * @param type of attribute
     * @param query to search
     * @return
     * <ul>
     *  <li>list of values for string and list attributes</li>
     *  <li>list of units for numeric attributes</li>
     *  <li>list of descriptions for boolean attributes</li>
     * </ul>
     * @throws IllegalArgumentException if an invalid type is passed
     * @throws UnsupportedOperationException if the passed type is subclass of {@link ProductAttribute}, but there is no defined operation for values
     */
    List<String> findValuesForAttributes(String type, String query);
}
