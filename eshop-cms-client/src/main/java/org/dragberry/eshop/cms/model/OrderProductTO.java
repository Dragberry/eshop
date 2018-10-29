package org.dragberry.eshop.cms.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductTO {

	private Long productArticleId;

	private Long productId;

	private String fullTitle;

	private String article;

	private String reference;

	private String mainImage;

	private BigDecimal price;
	
	private BigDecimal actualPrice;
}
