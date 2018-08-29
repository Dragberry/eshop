package org.dragberry.eshop.service;

import java.util.List;

import org.dragberry.eshop.model.product.ProductListItem;
import org.dragberry.eshop.model.common.ImageModel;
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
	 * Get the product details
	 * @param productReference
	 * @return
	 */
    ProductDetails getProduct(String productReference);

    /**
     * Get the product
     * @param productKey
     * @param imageKey 
     * @return
     */
    ImageModel getProductImage(Long productKey, Long imageKey);

}
