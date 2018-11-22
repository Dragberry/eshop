package org.dragberry.eshop.cms.mapper;

import org.dragberry.eshop.cms.model.ProductListItemTO;
import org.dragberry.eshop.dal.entity.Product;
import org.dragberry.eshop.dal.entity.ProductArticle;
import org.dragberry.eshop.utils.ProductTitleBuilder;

public class ProductMapper {

    private ProductMapper() {}
    
    public static ProductListItemTO map(Product product) {
        ProductListItemTO productTO = new ProductListItemTO();
        productTO.setProductId(product.getEntityKey());
        ProductArticle productArticle = product.getProductArticle();
        productTO.setProductArticleId(productArticle.getEntityKey());
        productTO.setArticle(productArticle.getArticle());
        productTO.setMainImage(product.getProductArticle().getMainImage().getPath());
        productTO.setPrice(product.getPrice());
        productTO.setActualPrice(product.getActualPrice());
        productTO.setTitle(productArticle.getTitle());
        productTO.setOptionsTitle(ProductTitleBuilder.buildOptionsTitle(product));
        return productTO;
    }
}
