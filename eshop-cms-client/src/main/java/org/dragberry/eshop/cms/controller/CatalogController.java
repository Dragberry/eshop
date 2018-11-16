package org.dragberry.eshop.cms.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.dragberry.eshop.cms.controller.exception.ResourceNotFoundException;
import org.dragberry.eshop.cms.model.ProductCategoryTO;
import org.dragberry.eshop.cms.model.ProductListItemTO;
import org.dragberry.eshop.cms.service.ProductCmsService;
import org.dragberry.eshop.common.PageableList;
import org.dragberry.eshop.service.DataImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatalogController {

	 @Autowired
    private ResourceLoader resourceLoader;
	
    @Autowired
    private ProductCmsService productService;
    
    @Autowired
    @Qualifier("InSalesDataImporter")
    private DataImporter inSalesDataImporter;
    
    @GetMapping("${cms.context}/catalog/products/import")
    public Boolean doImport() throws IOException {
    	inSalesDataImporter.importData(resourceLoader.getResource("classpath:data/insales_export.csv").getInputStream());
    	return true;
    }
    
    @GetMapping("${cms.context}/catalog/products")
    public PageableList<?> getProdcutList(
            @RequestParam(required = true) Integer pageNumber,
            @RequestParam(required = true) Integer pageSize,
            @RequestParam(required = false) Long categoryId,
            HttpServletRequest request) {
        return productService.getProducts(pageNumber - 1, pageSize, request.getParameterMap());
    }
    
    /**
     * Get the available product options for the given article
     * @param pageNumber
     * @param pageSize
     * @param query
     * @return
     */
    @GetMapping("${cms.context}/catalog/products/{productArticleId}/options")
    public List<ProductListItemTO> searchProducts(@PathVariable(required = true) Long productArticleId) {
        return productService.getProductOptions(productArticleId).orElseThrow(ResourceNotFoundException::new);
    }
    
    @GetMapping("${cms.context}/catalog/categories/tree")
    public List<ProductCategoryTO> categoryTree() {
    	return productService.getCategoryTree();
    }
}
