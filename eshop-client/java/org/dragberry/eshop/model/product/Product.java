package org.dragberry.eshop.model.product;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
	
	private Long id;
	
	private String article;
	
	private String title;

	private String url;
	
	private BigDecimal price;
	
	private BigDecimal oldPrice;
	
	private int repliesCount;
	
	private double averageMark;
	
}
