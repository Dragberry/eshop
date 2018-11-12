package org.dragberry.eshop.cms.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductListItemTO {

	private Long productArticleId;

	private Long productId;

	private String title;
	
	private String optionsTitle;

	private String article;

	private String mainImage;

	private BigDecimal price;
	
	private BigDecimal actualPrice;
}
