package org.dragberry.eshop.cms.mapper;

import java.util.function.BiFunction;

import org.dragberry.eshop.cms.model.OrderProductTO;
import org.dragberry.eshop.dal.entity.Product;
import org.dragberry.eshop.dal.entity.ProductArticle;
import org.dragberry.eshop.utils.ProductTitleBuilder;

public class OrderProductMapper {

    private OrderProductMapper() {}
    
    public static OrderProductTO map(Product product, BiFunction<Long, String, String> mainImageProvider) {
        OrderProductTO productTO = new OrderProductTO();
        productTO.setProductId(product.getEntityKey());
        ProductArticle productArticle = product.getProductArticle();
        productTO.setProductArticleId(productArticle.getEntityKey());
        productTO.setArticle(productArticle.getArticle());
        productTO.setMainImage(mainImageProvider.apply(productArticle.getEntityKey(), productArticle.getArticle()));
        productTO.setPrice(product.getPrice());
        productTO.setActualPrice(product.getActualPrice());
        productTO.setReference(productArticle.getReference());
        productTO.setTitle(productArticle.getTitle());
        productTO.setOptionsTitle(ProductTitleBuilder.buildOptionsTitle(product));
        return productTO;
    }
}
