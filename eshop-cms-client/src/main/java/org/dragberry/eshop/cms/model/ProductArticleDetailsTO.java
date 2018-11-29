package org.dragberry.eshop.cms.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.dragberry.eshop.dal.entity.ProductArticle.SaleStatus;
import org.dragberry.eshop.model.common.FileTO;
import org.dragberry.eshop.model.product.AttributeTO;
import org.dragberry.eshop.model.product.BooleanAttributeTO;
import org.dragberry.eshop.model.product.ListAttributeTO;
import org.dragberry.eshop.model.product.NumericAttributeTO;
import org.dragberry.eshop.model.product.StringAttributeTO;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
	
	@JsonIgnore
	public Stream<AttributeTO<?>> streamAttributes() {
	    return Stream.of(
	            booleanAttributes,
	            listAttributes,
	            numericAttributes,
	            stringAttributes)
	            .flatMap(List::stream);
	}
}
