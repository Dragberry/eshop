package org.dragberry.eshop.dal.entity;

import java.math.BigDecimal;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "PRODUCT")
@TableGenerator(
		name = "PRODUCT_GEN", 
		table = "GENERATOR",
		pkColumnName = "GEN_NAME", 
		pkColumnValue = "PRODUCT_GEN",
		valueColumnName = "GEN_VALUE",
		initialValue = 1000,
		allocationSize = 1)
public class Product extends BaseEntity {

	private static final long serialVersionUID = 6817451222642163283L;

	@Id
	@Column(name = "PRODUCT_KEY")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "PRODUCT_GEN")
	private Long entityKey;
	
	@Column(name = "TITLE")
	private String title;
	
	@Column(name = "ARTICLE")
	private String article;
	
	@Column(name = "OPTION")
	private String option;

	@Column(name = "QUANTITY")
	private Integer quantity;
	
	@Column(name = "REFERENCE")
	private String reference;
	
	@Column(name = "PRICE")
	private BigDecimal price;
	
	private Map<ProductOption, Integer> productOptions;

	class ProductOption {
		private String option;
		private String value;
		private Integer quantity;
	}
	
	@Override
	public Long getEntityKey() {
		return entityKey;
	}

	@Override
	public void setEntityKey(Long entityKey) {
		this.entityKey = entityKey;
	}
	
	
}
