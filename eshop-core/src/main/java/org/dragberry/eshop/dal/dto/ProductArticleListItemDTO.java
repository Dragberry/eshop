package org.dragberry.eshop.dal.dto;

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
    
}
