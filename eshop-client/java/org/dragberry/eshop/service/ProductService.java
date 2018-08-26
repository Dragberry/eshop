package org.dragberry.eshop.service;

import java.util.List;

import org.dragberry.eshop.model.product.Product;
import org.dragberry.eshop.model.product.ProductCategory;
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
	List<Product> getProductList(ProductSearchQuery query);

}
