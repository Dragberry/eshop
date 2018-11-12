package org.dragberry.eshop.dal.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductOrderItemDTO {

	private Long productId;
	
	private Long productArticleId;

	private String title;
	
	private String article;

	private BigDecimal price;
	
	private BigDecimal actualPrice;
}
