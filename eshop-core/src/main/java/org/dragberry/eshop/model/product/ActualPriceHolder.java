package org.dragberry.eshop.model.product;

import java.math.BigDecimal;

public interface ActualPriceHolder {

    void setPrice(BigDecimal price);
    
    BigDecimal getPrice();
    
    void setActualPrice(BigDecimal actualPrice);
    
    BigDecimal getActualPrice();
}
