package org.dragberry.eshop.dal.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import lombok.Getter;
import lombok.Setter;

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
@Setter
@Getter
public class Product extends AuditableEntity {

	private static final long serialVersionUID = 6817451222642163283L;

	@Id
	@Column(name = "PRODUCT_KEY")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "PRODUCT_GEN")
	private Long entityKey;
	
	@ManyToOne
	@JoinColumn(name = "PRODUCT_ARTICLE_KEY", referencedColumnName = "PRODUCT_ARTICLE_KEY")
	private ProductArticle productArticle;
	
	@ManyToMany
	@JoinTable(name = "PRODUCT_PRODUCT_ARTICLE_OPTION", 
        joinColumns = @JoinColumn(name = "PRODUCT_KEY", referencedColumnName = "PRODUCT_KEY"), 
        inverseJoinColumns = @JoinColumn(name = "PRODUCT_ARTICLE_OPTION_KEY", referencedColumnName = "PRODUCT_ARTICLE_OPTION_KEY"))
	private Set<ProductArticleOption> options;
	
	@Column(name = "PRICE")
    private BigDecimal price;
    
    @Column(name = "ACTUAL_PRICE")
    private BigDecimal actualPrice;
	
	@Column(name = "QUANTITY")
	private Integer quantity;
	
	@OneToMany(mappedBy = "product")
	private List<OrderItem> orderItems;
	
	@OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "MAIN_IMAGE_KEY", referencedColumnName = "FILE_KEY")
	private File mainImage;

	
}
