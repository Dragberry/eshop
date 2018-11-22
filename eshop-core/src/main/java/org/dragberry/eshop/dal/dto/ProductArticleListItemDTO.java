package org.dragberry.eshop.dal.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductArticleListItemDTO {

    private Long id;
    
    private String article;
    
    private String title;
    
    private BigDecimal price;
    
    private BigDecimal actualPrice;
    
    private Long optionsCount;
    
    private String mainImage;
    
}
