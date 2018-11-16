package org.dragberry.eshop.model.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dragberry.eshop.model.comment.CommentDetails;
import org.dragberry.eshop.model.common.KeyValue;
import org.dragberry.eshop.model.common.Modifier;
import org.dragberry.eshop.model.product.CategoryItem;

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
public class ProductDetails {

    private Long id;
    
    private String article;
    
    private String title;
    
    private String description;
    
    private String descriptionFull;

    private String reference;
    
    private CategoryItem category;
    
    private BigDecimal price;
    
    private BigDecimal actualPrice;
    
    private Map<String, Set<KeyValue>> optionValues;
    
    private Map<Long, Set<KeyValue>> productOptions;
    
    private Map<Long, BigDecimal> productPrices;
    
    private Map<Long, BigDecimal> productActualPrices;
    
    private String mainImage;
    
    private List<String> images;

    private Map<String, Modifier> labels;
    
    private String tagDescription;
    
    private String tagKeywords;
    
    private String tagTitle;
    
    private Map<String, List<KeyValue>> attributes = new LinkedHashMap<>();
    
    private List<CommentDetails> comments = new ArrayList<>(); 
}
