package org.dragberry.eshop.service;

import java.util.List;

import org.dragberry.eshop.common.ResultTO;
import org.dragberry.eshop.model.comment.CommentDetails;
import org.dragberry.eshop.model.comment.ProductCommentRequest;
import org.dragberry.eshop.model.comment.ProductCommentResponse;

public interface CommentService {

	/**
	 * Create c product comment
	 * @param comment
	 * @return
	 */
	ResultTO<ProductCommentResponse> createComment(ProductCommentRequest comment);
	
	/**
	 * Get the list of product comments
	 * @return
	 */
	List<CommentDetails> getCommentList();
}
