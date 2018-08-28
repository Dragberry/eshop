package org.dragberry.eshop.controller;

import java.util.List;

import org.dragberry.eshop.controller.exception.ResourceNotFoundException;
import org.dragberry.eshop.model.product.ProductCategory;
import org.dragberry.eshop.model.product.ProductDetails;
import org.dragberry.eshop.model.product.ProductSearchQuery;
import org.dragberry.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	/**
	 * Return a list of products
	 * @return
	 */
	@GetMapping({"${url.catalog}", "${url.catalog}" + "/{selectedCategory}"})
	public ModelAndView catalog(@PathVariable(required = false) String selectedCategory) {
		ModelAndView mv = new ModelAndView("pages/products/product-list");
		List<ProductCategory> categoryList = productService.getCategoryList();
		mv.addObject("categoryList", categoryList);
		ProductCategory category = categoryList.stream().filter(c -> c.getReference().equals(selectedCategory)).findFirst().orElse(new ProductCategory(0L, "all", "Все товары"));
		mv.addObject("category", category);
		ProductSearchQuery query = new ProductSearchQuery();
		query.setCategoryName(selectedCategory);
		mv.addObject("productList", productService.getProductList(query));
		return mv;
	}

	/**
     * Return a product page
     * @return
     */
    @GetMapping({"${url.product}" + "/{productReference}"})
    public ModelAndView prodcut(@PathVariable String productReference) {
        if (productReference != null) {
            ProductDetails product = productService.getProduct(productReference);
            if (product != null) {
                ModelAndView mv = new ModelAndView("pages/products/product");
                mv.addObject("product", product);
                return mv;
            }
        }
        throw new ResourceNotFoundException();
    }
}