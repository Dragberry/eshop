package org.dragberry.eshop.dal.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ProductCommentId implements Serializable {

	private static final long serialVersionUID = 398589904838582484L;

	@Column(name = "PRODUCT_ARTICLE_KEY")
	private Long productArticleId;
	
	@Column(name = "COMMENT_KEY")
	private Long commentId;
}
