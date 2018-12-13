
package org.dragberry.eshop.cms.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.dragberry.eshop.cms.controller.exception.BadRequestException;
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
    
    /**
     * Search for available groups for product attributes
     * @param query to search
     * @return list of available groups
     */
    @GetMapping("${cms.context}/catalog/products/attributes/groups")
    public List<String> findGroupsForAttributes(@RequestParam(required = true) String query) {
        return productService.findGroupsForAttributes(query);
    }
    
    /**
     * Search for available name for the given product attribute type
     * @param type of attribute
     * @param query to search
     * @return list of available names
     */
    @GetMapping("${cms.context}/catalog/products/attributes/names")
    public List<String> findNamesForAttributes(
            @RequestParam(required = true) String type,
            @RequestParam(required = true) String query) {
        try {
            return productService.findNamesForAttributes(type, query);
        } catch (IllegalArgumentException iae) {
            throw new BadRequestException();
        }
    }
    
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
     */
    @GetMapping("${cms.context}/catalog/products/attributes/values")
    public List<String> findValuesForAttributes(
            @RequestParam(required = true) String type,
            @RequestParam(required = true) String query) {
        try {
            return productService.findValuesForAttributes(type, query);
        } catch (IllegalArgumentException iae) {
            throw new BadRequestException();
        }
    }
}
