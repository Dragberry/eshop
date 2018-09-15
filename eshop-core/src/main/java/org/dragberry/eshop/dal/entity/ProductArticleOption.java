package org.dragberry.eshop.dal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PRODUCT_ARTICLE_OPTION")
@TableGenerator(
		name = "PRODUCT_ARTICLE_OPTION_GEN", 
		table = "GENERATOR",
		pkColumnName = "GEN_NAME", 
		pkColumnValue = "PRODUCT_ARTICLE_OPTION_GEN",
		valueColumnName = "GEN_VALUE",
		initialValue = 1000,
		allocationSize = 1)
@Setter
@Getter
public class ProductArticleOption extends BaseEntity {

	private static final long serialVersionUID = 6817451222642163283L;

	@Id
	@Column(name = "PRODUCT_ARTICLE_OPTION_KEY")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "PRODUCT_ARTICLE_OPTION_GEN")
	private Long entityKey;
	
	@ManyToOne
	@JoinColumn(name = "PRODUCT_ARTICLE_KEY", referencedColumnName = "PRODUCT_ARTICLE_KEY")
	private ProductArticle productArticle;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "VALUE")
    private String value;
	
}
