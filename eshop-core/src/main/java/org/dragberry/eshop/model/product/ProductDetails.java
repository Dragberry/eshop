package org.dragberry.eshop.model.product;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dragberry.eshop.model.common.KeyValue;
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
    
    private String descriptionFull;

    private String reference;
    
    private BigDecimal actualPrice;
    
    private BigDecimal price;
    
    private CategoryItem category;
    
    private Map<String, Set<KeyValue>> optionValues;
    
    private Map<Long, Set<KeyValue>> productOptions;
    
    private Long mainImage;
    
    private List<Long> images;

    private Map<String, Modifier> labels;
    
    private String tagDescription;
    
    private String tagKeywords;
    
    private String tagTitle;
    
    private Map<String, List<KeyValue>> attributes = new LinkedHashMap<>();
}
