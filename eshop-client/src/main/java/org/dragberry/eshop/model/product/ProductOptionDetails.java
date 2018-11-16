package org.dragberry.eshop.model.product;

import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

import org.dragberry.eshop.model.common.KeyValue;

import lombok.Getter;

@Getter
@Setter
public class ProductOptionDetails {

	private List<KeyValue> options;
	
	private BigDecimal price;
	
	private BigDecimal actualPrice;
}
