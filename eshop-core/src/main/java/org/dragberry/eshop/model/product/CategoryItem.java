package org.dragberry.eshop.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryItem {

    private Long id;
    
    private String name;
    
    private String reference;
    
}
