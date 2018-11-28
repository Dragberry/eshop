package org.dragberry.eshop.cms.model;

import java.util.ArrayList;
import java.util.List;

import org.dragberry.eshop.dal.entity.ProductArticle.SaleStatus;
import org.dragberry.eshop.model.common.FileTO;
import org.dragberry.eshop.model.product.BooleanAttributeTO;
import org.dragberry.eshop.model.product.ListAttributeTO;
import org.dragberry.eshop.model.product.NumericAttributeTO;
import org.dragberry.eshop.model.product.StringAttributeTO;

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
	
	private Long mainImageId;
	
	private List<FileTO> images = new ArrayList<>();
	
	private Long mainCategoryId;
	
	private List<ProductCategoryTO> categoryTree = new ArrayList<>();
	
	private List<StringAttributeTO> stringAttributes = new ArrayList<>();
	
	private List<ListAttributeTO> listAttributes = new ArrayList<>();
	
	private List<NumericAttributeTO> numericAttributes = new ArrayList<>();
	
	private List<BooleanAttributeTO> booleanAttributes = new ArrayList<>();
}
