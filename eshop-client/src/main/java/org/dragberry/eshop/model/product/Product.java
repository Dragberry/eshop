package org.dragberry.eshop.model.product;

import java.math.BigDecimal;
import java.util.Map;

import org.dragberry.eshop.model.bootstrap.Modifier;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
	
	private Long id;
	
	private String article;
	
	private String title;

	private String reference;
	
	private BigDecimal price;
	
	private BigDecimal oldPrice;
	
	private int commentsCount;
	
	private double rating;
	
	private Map<String, Modifier> labels;
	
	private String mainImage;
}
