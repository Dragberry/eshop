package org.dragberry.eshop.model.cart;


import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuickOrderDetails {
    
    private Long id;
    
    private String phone;
    
    private String fullName;
    
    private String address;
    
    private Long productId;
    
    private String productArticle;
    
    private String productFullTitle;

    private BigDecimal productPrice;
}