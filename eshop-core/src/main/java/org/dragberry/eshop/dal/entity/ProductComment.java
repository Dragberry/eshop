package org.dragberry.eshop.dal.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;


@Entity
@Table(name = "PRODUCT_COMMENT")
@Data
public class ProductComment {

	@EmbeddedId
	private ProductCommentId entityKey;
	
	@MapsId("productArticleId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ARTICLE_KEY", referencedColumnName = "PRODUCT_ARTICLE_KEY")
	private ProductArticle productArticle;
	
	@MapsId("commentId")
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "COMMENT_KEY", referencedColumnName = "COMMENT_KEY")
	private Comment comment;
	
	@Column(name = "MARK")
	private Integer mark;
}
