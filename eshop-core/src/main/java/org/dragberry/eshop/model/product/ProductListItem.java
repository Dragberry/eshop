package org.dragberry.eshop.model.product;

import java.math.BigDecimal;
import java.util.Map;

import org.dragberry.eshop.model.common.Modifier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListItem {
	
	private Long id;
	
	private String article;
	
	private String title;

	private String reference;
	
	private BigDecimal actualPrice;
	
	private BigDecimal price;
	
	private int commentsCount;
	
	private double rating;
	
	private Map<String, Modifier> labels;
	
	private Long mainImage;
}
