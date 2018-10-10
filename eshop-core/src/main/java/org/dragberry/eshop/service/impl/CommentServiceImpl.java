package org.dragberry.eshop.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.dragberry.eshop.common.IssueTO;
import org.dragberry.eshop.common.Issues;
import org.dragberry.eshop.common.ResultTO;
import org.dragberry.eshop.common.Results;
import org.dragberry.eshop.dal.entity.Category;
import org.dragberry.eshop.dal.entity.Comment;
import org.dragberry.eshop.dal.entity.Comment.Status;
import org.dragberry.eshop.dal.entity.ProductArticle;
import org.dragberry.eshop.dal.repo.CategoryRepository;
import org.dragberry.eshop.dal.repo.CommentRepository;
import org.dragberry.eshop.dal.repo.ProductArticleRepository;
import org.dragberry.eshop.model.comment.CommentDetails;
import org.dragberry.eshop.model.comment.ProductCommentDetails;
import org.dragberry.eshop.model.comment.ProductCommentRequest;
import org.dragberry.eshop.model.comment.ProductCommentResponse;
import org.dragberry.eshop.service.CommentService;
import org.dragberry.eshop.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private ProductArticleRepository productArticleRepo;
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
	private CommentRepository commentRepo;
	
	@Autowired
	private ImageService imageService;
	
	@Override
	@Transactional
	public ResultTO<ProductCommentResponse> createComment(ProductCommentRequest comment) {
		List<IssueTO> issues = new ArrayList<>();
		if (StringUtils.isBlank(comment.getName())) {
			issues.add(Issues.error("msg.error.comment.name.required", "addCommentName"));
		} else if (!GenericValidator.maxLength(comment.getName(), 32)) {
			issues.add(Issues.error("msg.error.comment.name.tooLong", "addCommentName"));
		}
		
		if (StringUtils.isBlank(comment.getEmail())) {
			issues.add(Issues.error("msg.error.common.email.required", "addCommentEmail"));
		} else if (!GenericValidator.maxLength(comment.getEmail(), 128)) {
			issues.add(Issues.error("msg.error.common.email.tooLong", "addCommentEmail"));
		} else if (!EmailValidator.getInstance().isValid(comment.getEmail())) {
			issues.add(Issues.error("msg.error.common.email.invalid", "addCommentEmail"));
		}
		
		if (StringUtils.isBlank(comment.getText())) {
			issues.add(Issues.error("msg.error.comment.comment.required", "addCommentText"));
		} else if (!GenericValidator.maxLength(comment.getText(), 1024)) {
			issues.add(Issues.error("msg.error.comment.comment.tooLong", "addCommentText"));
		}
		
		if (comment.getMark() == null) {
			issues.add(Issues.error("msg.error.comment.rating.required", "addCommentRating"));
		} else if (!GenericValidator.isInRange(comment.getMark(), 1, 5)) {
			issues.add(Issues.error("msg.error.comment.rating.invalid", "addCommentRating"));
		}
		
		if (comment.getProductId() == null) {
			issues.add(Issues.error("msg.error.comment.product.required", "addCommentProduct"));
		} else {
		    LocalDateTime lastCommentTime = commentRepo.findLastUserComment(comment.getIp());
		    if (lastCommentTime != null && LocalDateTime.now().minusMinutes(2).isBefore(lastCommentTime)) {
		        issues.add(Issues.error("msg.error.comment.tooEarly", "addCommentProduct"));
		    } else {
    			Optional<ProductArticle> paOpt = productArticleRepo.findById(comment.getProductId());
    			if (!paOpt.isPresent()) {
    				issues.add(Issues.error("msg.error.comment.product.invalid", "addCommentProduct"));
    			} else if (issues.isEmpty()) {
    				Comment cmt = new Comment();
    				cmt.setStatus(Status.ACTIVE);
    				cmt.setText(comment.getText());
    				cmt.setUserIP(comment.getIp());
    				cmt.setUserName(comment.getName());
    				cmt.setDateTime(LocalDateTime.now());
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
		}
		return Results.create(null, issues);
	}

	@Override
	public List<CommentDetails> getCommentList() {
	    return commentRepo.findProductComments(PageRequest.of(0, 20)).stream().map(dto -> {
	    	ProductCommentDetails cd = new ProductCommentDetails();
			cd.setDate(dto.getDate());
	        cd.setId(dto.getId());
	        cd.setName(dto.getName());
	        cd.setText(dto.getText());
	        cd.setMark(dto.getMark());
			cd.setProductId(dto.getProductId());
	        cd.setProductTitle(dto.getProductTitle());
	        cd.setProductArticle(dto.getProductArticle());
	        cd.setProductImage(imageService.findMainImage(dto.getProductId(), dto.getProductArticle()));
	        cd.setProductReference(dto.getProductReference());
	        Category ctg = categoryRepo.findByProductId(dto.getId()).get(0);
	        cd.setProductCategoryReference(ctg.getReference());
	        return cd;
	    }).collect(Collectors.toList());
	}
}
