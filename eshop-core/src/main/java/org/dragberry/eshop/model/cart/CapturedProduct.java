package org.dragberry.eshop.model.cart;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.dragberry.eshop.model.common.KeyValue;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "productId")
public class CapturedProduct {

    private Long productArticleId;
    
    private Long productId;

    private String title;
    
    private String article;
    
    private BigDecimal price;
    
    private Set<KeyValue> options  = new HashSet<>();
}
