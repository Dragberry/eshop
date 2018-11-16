package org.dragberry.eshop.model.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCommentDetails extends CommentDetails {

	private Long productId;
	
	private String productArticle;
	
	private String productTitle;
	
	private String productImage;
	
	private String productCategoryReference;
	
	private String productReference;
}
