package org.dragberry.eshop.dal.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductCommentDTO {
	
	private Long id;
	
	private String name;
	
	private String text;
	
	private LocalDateTime date;

	private Integer mark;
	
	private Long productId;
	
	private String productArticle;
	
	private String productTitle;
	
	private String productReference;
	
	private String categoryReference;
	
	private String mainImage;
}
