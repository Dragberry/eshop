package org.dragberry.eshop.cms.model;

import org.dragberry.eshop.dal.entity.ProductArticle.SaleStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductArticleDetailsTO {

	private Long id;
	
	private String title;
	
	private String article;
	
	private String reference;
	
	private String description;
	
	private String descriptionFull;
	
	private String tagTitle;
	
	private String tagKeywords;
	
	private String tagDescription;
	
	private SaleStatus status;
}
