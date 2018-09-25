package org.dragberry.eshop.service;

import org.dragberry.eshop.common.ResultTO;
import org.dragberry.eshop.model.comment.ProductCommentRequest;
import org.dragberry.eshop.model.comment.ProductCommentResponse;

public interface CommentService {

	public ResultTO<ProductCommentResponse> createComment(ProductCommentRequest comment);
	
}
