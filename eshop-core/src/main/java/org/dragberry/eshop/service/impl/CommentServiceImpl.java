package org.dragberry.eshop.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.dragberry.eshop.common.IssueTO;
import org.dragberry.eshop.common.Issues;
import org.dragberry.eshop.common.ResultTO;
import org.dragberry.eshop.common.Results;
import org.dragberry.eshop.dal.entity.Comment;
import org.dragberry.eshop.dal.entity.Comment.Status;
import org.dragberry.eshop.dal.entity.ProductArticle;
import org.dragberry.eshop.dal.repo.CommentRepository;
import org.dragberry.eshop.dal.repo.ProductArticleRepository;
import org.dragberry.eshop.model.comment.ProductCommentRequest;
import org.dragberry.eshop.model.comment.ProductCommentResponse;
import org.dragberry.eshop.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private ProductArticleRepository productArticleRepo;
	
	@Autowired
	private CommentRepository commentRepo;
	
	@Override
	@Transactional
	public ResultTO<ProductCommentResponse> createComment(ProductCommentRequest comment) {
		List<IssueTO> issues = new ArrayList<>();
		if (StringUtils.isBlank(comment.getName())) {
			issues.add(Issues.error("msg.error.comment.name.required", "name"));
		} else if (!GenericValidator.maxLength(comment.getName(), 32)) {
			issues.add(Issues.error("msg.error.comment.name.tooLong", "name"));
		}
		
		if (StringUtils.isBlank(comment.getEmail())) {
			issues.add(Issues.error("msg.error.common.email.required", "email"));
		} else if (!GenericValidator.maxLength(comment.getEmail(), 128)) {
			issues.add(Issues.error("msg.error.common.email.tooLong", "email"));
		} else if (!EmailValidator.getInstance().isValid(comment.getEmail())) {
			issues.add(Issues.error("msg.error.common.email.invalid", "email"));
		}
		
		if (StringUtils.isBlank(comment.getText())) {
			issues.add(Issues.error("msg.comment.comment.required", "comment"));
		} else if (!GenericValidator.maxLength(comment.getText(), 1024)) {
			issues.add(Issues.error("msg.comment.comment.tooLong", "comment"));
		}
		
		if (comment.getMark() == null) {
			issues.add(Issues.error("msg.error.comment.rating.required", "productRating"));
		} else if (!GenericValidator.isInRange(comment.getMark(), 1, 5)) {
			issues.add(Issues.error("msg.error.comment.rating.invalid", "productRating"));
		}
		
		if (comment.getProductId() == null) {
			issues.add(Issues.error("msg.error.comment.product.required", "productId"));
		} else {
			Optional<ProductArticle> paOpt = productArticleRepo.findById(comment.getProductId());
			if (!paOpt.isPresent()) {
				issues.add(Issues.error("msg.error.comment.product.invalid", "productId"));
			} else if (issues.isEmpty()) {
				Comment cmt = new Comment();
				cmt.setStatus(Status.ACTIVE);
				cmt.setText(comment.getText());
				cmt.setUserIP(comment.getIp());
				cmt.setUserName(comment.getName());
				cmt = commentRepo.save(cmt);
				ProductArticle pa = paOpt.get();
				pa.addComment(cmt, comment.getMark());
				pa = productArticleRepo.save(pa);
				ProductCommentResponse resp = new ProductCommentResponse();
				resp.setDate(cmt.getCreatedDate());
				resp.setId(cmt.getEntityKey());
				resp.setMark(comment.getMark());
				resp.setName(cmt.getUserName());
				resp.setText(cmt.getText());
				resp.setProductId(pa.getEntityKey());
				return Results.create(resp);
			}
		}
		return Results.create(null, issues);
	}

}
