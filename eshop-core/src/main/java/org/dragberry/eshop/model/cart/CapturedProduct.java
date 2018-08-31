package org.dragberry.eshop.model.cart;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.dragberry.eshop.model.common.KeyValue;

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
    
    private Long mainImage;
    
    private BigDecimal price;
    
    private Integer quantity;
    
    private BigDecimal totalPrice;
    
    private Set<KeyValue> options  = new HashSet<>();
    
    public void updateFullTitle() {
        this.fullTitle = title + options.stream().map(option -> option.getKey() + ": " + option.getValue()).collect(Collectors.joining("; ", " (", ")"));
    }
    
}
