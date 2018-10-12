package org.dragberry.eshop.service;

import java.util.List;
import java.util.Map;

import org.dragberry.eshop.model.product.ProductListItem;
import org.dragberry.eshop.model.cart.CapturedProduct;
import org.dragberry.eshop.model.product.Filter;
import org.dragberry.eshop.model.product.ProductArticleOptions;
import org.dragberry.eshop.model.product.ProductCategory;
import org.dragberry.eshop.model.product.ProductDetails;
import org.dragberry.eshop.model.product.ProductSearchQuery;

public interface ProductService {
	
	/**
	 * Gets the category list for side menu
	 * @return
	 */
	List<ProductCategory> getCategoryList();
	
	/**
	 * Gets the product list
	 * @return
	 */
	List<ProductListItem> getProductList(ProductSearchQuery query);
	
	/**
     * Get the result of the search by string query
     * @param query
     * @return
     */
    List<ProductListItem> getProductList(String query, Map<String, String[]> searchParams);

    /**
     * Get available product with options for the given product article id
     * @param productArticleId
     * @return
     */
    ProductArticleOptions getProductOptions(Long productArticleId);
    
	/**
	 * Get the product details
	 * @param categoryReference
	 * @param productReference
	 * @return
	 */
    ProductDetails getProductArticleDetails(String categoryReference, String productReference);

    /**
     * Get details for captured product
     * @param productId
     * @return
     */
    CapturedProduct getProductCartDetails(Long productId);

    /**
     * Find a category by reference
     * @param categoryReference
     * @return
     */
    ProductCategory findCategory(String categoryReference);

    /**
     * Get filters for the given categoryId
     * @param categoryId
     * @return
     */
	List<Filter> getCategoryFilters(Long categoryId);

}
