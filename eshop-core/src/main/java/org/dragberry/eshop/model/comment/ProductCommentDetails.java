package org.dragberry.eshop.model.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCommentDetails extends CommentDetails {

	private Long productArticleId;
	
	private String productTitle;
	
}
