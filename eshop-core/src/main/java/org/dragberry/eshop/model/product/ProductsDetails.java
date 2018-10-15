package org.dragberry.eshop.model.product;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import org.dragberry.eshop.model.common.KeyValue;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductsDetails {
    
    private Map<String, Set<KeyValue>> optionValues;
    
    private Map<Long, Set<KeyValue>> productOptions;
    
    private Map<Long, BigDecimal> productPrices;
    
    private Map<Long, BigDecimal> productActualPrices;

}
