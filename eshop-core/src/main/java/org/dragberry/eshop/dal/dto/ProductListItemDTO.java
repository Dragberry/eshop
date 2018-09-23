package org.dragberry.eshop.dal.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductListItemDTO {

	private Long id;
	
	private String title;
	
	private String article;
	
	private String reference;
	
	private Long mainImage;
	
	private BigDecimal actualPrice;
	
	private BigDecimal price;
	
	private Long commentsCount;
	
	private Double averageMark;
	
}
