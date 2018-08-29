package org.dragberry.eshop.model.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.dragberry.eshop.model.common.Modifier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents full product details
 * @author Drahun Maksim
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetails implements ActualPriceHolder {

    private Long id;
    
    private String article;
    
    private String title;
    
    private String description;

    private String reference;
    
    private BigDecimal actualPrice;
    
    private BigDecimal price;
    
    private Map<String, Modifier> labels;
    
    private Long mainImage;
    
    private List<String> images;
}
