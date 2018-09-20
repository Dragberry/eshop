package org.dragberry.eshop.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dragberry.eshop.controller.exception.ResourceNotFoundException;
import org.dragberry.eshop.model.common.ImageModel;
import org.dragberry.eshop.model.product.ProductCategory;
import org.dragberry.eshop.model.product.ProductDetails;
import org.dragberry.eshop.model.product.ProductSearchQuery;
import org.dragberry.eshop.navigation.Breadcrumb;
import org.dragberry.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProductController {
	
	private static final String MODEL_CATEGORY = "category";

	private static final String MODEL_PRODUCT_LIST = "productList";

	private static final String MODEL_CATEGORY_LIST = "categoryList";

	private static final String MODEL_BREADCRUMB = "breadcrumb";

	private static final String MSG_MENU_CATALOG = "msg.menu.catalog";

	@Value("${url.catalog}")
	private String catelogReference;
	
	@Autowired
	private ProductService productService;
	
	/**
     * Serves images
     * @return
	 * @throws IOException 
     */
    @GetMapping({"${url.products-images}/{productKey}/{imageKey}/{imageName}"})
    public void productMainImage(HttpServletResponse response,
            @PathVariable Long productKey,
            @PathVariable Long imageKey,
            @PathVariable String imageName) throws IOException {
        ImageModel img = productService.getProductImage(productKey, imageKey);
        if (img != null) {
            response.setContentType(img.getType());
            IOUtils.copy(new ByteArrayInputStream(img.getContent()), response.getOutputStream());
        }
    }
	
    @GetMapping("${url.catalog.filter}/{selectedCategory}")
    public ModelAndView search(HttpServletRequest request, @PathVariable String selectedCategory) {
    	ProductCategory category = productService.findCategory(selectedCategory);
    	if (category == null) {
    	    throw new ResourceNotFoundException();
    	}
        ModelAndView mv = new ModelAndView("pages/products/product-list :: products");
    	ProductSearchQuery query = new ProductSearchQuery();
    	query.setCategoryReference(selectedCategory);
    	query.setSearchParams(request.getParameterMap());
    	mv.addObject(MODEL_CATEGORY, category);
        mv.addObject(MODEL_PRODUCT_LIST, productService.getProductList(query));
    	return mv;
    }
    
    
	/**
	 * Return a list of products
	 * @return
	 */
	@GetMapping({"${url.catalog}", "${url.catalog}/{selectedCategory}"})
	public ModelAndView catalog(@PathVariable(required = false) String selectedCategory) {
		ModelAndView mv = new ModelAndView("pages/products/product-list");
		List<ProductCategory> categoryList = productService.getCategoryList();
		ProductSearchQuery query = new ProductSearchQuery();
		query.setCategoryReference(selectedCategory);
		if (StringUtils.isNotBlank(selectedCategory)) {
			ProductCategory category = categoryList.stream().filter(c -> c.getReference().equals(selectedCategory)).findFirst().orElse(new ProductCategory(0L, "all", "Все товары"));
			if (category == null) {
				throw new ResourceNotFoundException();
			} else {
				category.setFilters(productService.getCategoryFilters(category.getId()));
			}
			mv.addObject(MODEL_CATEGORY, category);
			mv.addObject(MODEL_BREADCRUMB, Breadcrumb.builder()
		     		.append(MSG_MENU_CATALOG, catelogReference, true)
		     		.append(category.getName(), selectedCategory));
		} else {
			mv.addObject(MODEL_BREADCRUMB, Breadcrumb.builder()
		     		.append(MSG_MENU_CATALOG, catelogReference, true));
		}
		mv.addObject(MODEL_CATEGORY_LIST, categoryList);
		mv.addObject(MODEL_PRODUCT_LIST, productService.getProductList(query));
		return mv;
	}

	/**
     * Return a product page
     * @return
     */
    @GetMapping({"${url.catalog}/{categoryReference}/{productReference}"})
    public ModelAndView product(@PathVariable String categoryReference, @PathVariable String productReference) {
        if (productReference != null) {
            ProductDetails product = productService.getProductArticleDetails(categoryReference, productReference);
            if (product != null) {
            	ModelAndView mv = new ModelAndView("pages/products/product");
                mv.addObject("product", product);
                mv.addObject(MODEL_BREADCRUMB, Breadcrumb.builder()
                		.append(MSG_MENU_CATALOG, catelogReference, true)
                		.append(product.getCategory().getName(), categoryReference)
                		.append(product.getTitle(), productReference));
                return mv;
            }
        }
        throw new ResourceNotFoundException();
    }
}