package org.dragberry.eshop.dal.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PRODUCT_ARTICLE")
@TableGenerator(
		name = "PRODUCT_ARTICLE_GEN", 
		table = "GENERATOR",
		pkColumnName = "GEN_NAME", 
		pkColumnValue = "PRODUCT_ARTICLE_GEN",
		valueColumnName = "GEN_VALUE",
		initialValue = 1000,
		allocationSize = 1)
@Setter
@Getter
public class ProductArticle extends BaseEntity {

	private static final long serialVersionUID = 6817451222642163283L;

	@Id
	@Column(name = "PRODUCT_ARTICLE_KEY")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "PRODUCT_ARTICLE_GEN")
	private Long entityKey;
	
	@Column(name = "TITLE")
	private String title;
	
	@Column(name = "ARTICLE")
	private String article;
	
	@Column(name = "REFERENCE")
	private String reference;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MAIN_IMAGE_KEY", referencedColumnName = "IMAGE_KEY")
	private Image mainImage;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "DESCRIPTION_FULL")
	private String descriptionFull;
	
	@OneToMany(mappedBy = "productArticle")
    private List<ProductArticleOption> options = new ArrayList<>();;
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "PRODUCT_ARTICLE_CATEGORY", 
        joinColumns = @JoinColumn(name = "PRODUCT_ARTICLE_KEY", referencedColumnName = "PRODUCT_ARTICLE_KEY"), 
        inverseJoinColumns = @JoinColumn(name = "CATEGORY_KEY", referencedColumnName = "CATEGORY_KEY"))
	private List<Category> categories;
	
	@OneToMany(mappedBy = "productArticle")
	private List<Product> products = new ArrayList<>();
	
	@Column(name = "TAG_TITLE")
	private String tagTitle;
	
	@Column(name = "TAG_KEYWORDS")
	private String tagKeywords;
	
	@Column(name = "TAG_DESCRIPTION")
	private String tagDescription;
	
}
