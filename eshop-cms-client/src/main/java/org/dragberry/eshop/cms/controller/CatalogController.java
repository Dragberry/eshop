package org.dragberry.eshop.cms.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.dragberry.eshop.cms.controller.exception.ResourceNotFoundException;
import org.dragberry.eshop.cms.model.ProductArticleDetailsTO;
import org.dragberry.eshop.cms.model.ProductCategoryTO;
import org.dragberry.eshop.cms.model.ProductListItemTO;
import org.dragberry.eshop.cms.service.ProductCmsService;
import org.dragberry.eshop.common.PageableList;
import org.dragberry.eshop.common.ResultTO;
import org.dragberry.eshop.common.Results;
import org.dragberry.eshop.service.DataImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

@Log4j2
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
    
    @GetMapping("${cms.context}/catalog/products/{productArticleId}")
    public ProductArticleDetailsTO productArticleDetails(@PathVariable(required = true) Long productArticleId) {
    	return productService.getProductArticleDetails(productArticleId).orElseThrow(ResourceNotFoundException::new);
    }
    
    @PutMapping("${cms.context}/catalog/products/{productArticleId}")
    @ResponseBody
    public ResultTO<ProductArticleDetailsTO> updateProductArticleDetails(
            @PathVariable(required = true) Long productArticleId,
            @RequestBody ProductArticleDetailsTO product) {
    	log.info(product);
    	return Results.create(product);
    }
    
    @PostMapping("${cms.context}/catalog/products/{productArticleId}/attributes")
    @ResponseBody
    public ResultTO<ProductArticleDetailsTO> updateProductAttributes(
            @PathVariable(required = true) Long productArticleId,
            @RequestBody ProductArticleDetailsTO product) {
        return productService.updateAttributes(productArticleId, product).orElseThrow(ResourceNotFoundException::new);
    }
    
}
