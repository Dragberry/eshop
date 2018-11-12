package org.dragberry.eshop.cms.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductArticleListItemTO {

    private Long id;
    
    private String article;
    
    private String title;
    
    private BigDecimal price;
    
    private BigDecimal actualPrice;
    
    private Long optionsCount;
    
    private String mainImage;
    
}
