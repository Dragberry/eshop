package org.dragberry.eshop.model.product;

import java.math.BigDecimal;
import java.util.Map;

import org.dragberry.eshop.model.common.Modifier;
import org.dragberry.eshop.model.product.CategoryItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListItem implements ActualPriceHolder {
	
	private Long id;
	
	private String article;
	
	private String title;

	private String reference;
	
	private String description;
	
	private BigDecimal actualPrice;
	
	private BigDecimal price;
	
	private Long commentsCount;
	
	private Double rating;
	
	private Map<String, Modifier> labels;
	
	private String mainImage;
	
	private CategoryItem category;
}
