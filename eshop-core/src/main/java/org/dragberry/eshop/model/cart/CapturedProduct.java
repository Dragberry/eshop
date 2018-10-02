package org.dragberry.eshop.model.cart;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.dragberry.eshop.model.common.KeyValue;
import org.dragberry.eshop.model.product.CategoryItem;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "productId")
public class CapturedProduct {

    private Long productArticleId;
    
    private Long productId;

    private String title;
    
    private String fullTitle;
    
    private String article;
    
    private String reference;
    
    private String mainImage;
    
    private BigDecimal price;
    
    private Integer quantity;
    
    private BigDecimal totalPrice;
    
    private CategoryItem category;
    
    private Set<KeyValue> options  = new HashSet<>();
    
    public void updateFullTitle() {
        this.fullTitle = ProductFullTitleBuilder.buildFullTitle(title, options);
    }
    
}
