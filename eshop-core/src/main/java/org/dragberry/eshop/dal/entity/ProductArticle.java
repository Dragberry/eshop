package org.dragberry.eshop.dal.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.dragberry.eshop.dal.entity.converter.SaleStatusConverter;

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
public class ProductArticle extends AuditableEntity {

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
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "DESCRIPTION_FULL")
	private String descriptionFull;
	
	@OneToMany(mappedBy = "productArticle")
    private List<ProductArticleOption> options = new ArrayList<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "PRODUCT_ARTICLE_CATEGORY", 
        joinColumns = @JoinColumn(name = "PRODUCT_ARTICLE_KEY", referencedColumnName = "PRODUCT_ARTICLE_KEY"), 
        inverseJoinColumns = @JoinColumn(name = "CATEGORY_KEY", referencedColumnName = "CATEGORY_KEY"))
	private List<Category> categories = new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_KEY", referencedColumnName = "CATEGORY_KEY")
	private Category category;
	
	@OneToMany(mappedBy = "productArticle")
	private List<Product> products = new ArrayList<>();
	
	@Column(name = "TAG_TITLE")
	private String tagTitle;
	
	@Column(name = "TAG_KEYWORDS")
	private String tagKeywords;
	
	@Column(name = "TAG_DESCRIPTION")
	private String tagDescription;
	
	@OrderBy("`ORDER`")
	@OneToMany(mappedBy = "productArticle", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductAttribute<?>> attributes = new ArrayList<>();
	
	@OneToMany(mappedBy = "productArticle", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductComment> comments = new ArrayList<>();
	
	@Column(name = "SALE_STATUS")
    @Convert(converter = SaleStatusConverter.class)
	private SaleStatus saleStatus;
	
	@ElementCollection
	@CollectionTable(name = "PRODUCT_LABEL", joinColumns = @JoinColumn(name = "PRODUCT_ARTICLE_KEY"))
	@MapKeyColumn(name = "LABEL")
	@Column(name = "TYPE")
	@Enumerated(EnumType.STRING)
	private Map<String, ProductLabelType> labels = new HashMap<>();
	
	@OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "MAIN_IMAGE_KEY", referencedColumnName = "FILE_KEY")
	private File mainImage;

	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "PRODUCT_ARTICLE_IMAGE", 
        joinColumns = @JoinColumn(name = "PRODUCT_ARTICLE_KEY", referencedColumnName = "PRODUCT_ARTICLE_KEY"), 
        inverseJoinColumns = @JoinColumn(name = "IMAGE_KEY", referencedColumnName = "FILE_KEY"))
	private List<File> images = new ArrayList<>();
	

	public void addComment(Comment comment, Integer mark) {
		ProductComment pComment = new ProductComment();
		pComment.setProductArticle(this);
		pComment.setEntityKey(new ProductCommentId(entityKey, comment.getEntityKey()));
		pComment.setComment(comment);
		pComment.setMark(mark);
		comments.add(pComment);
	}
	
	public static enum SaleStatus implements BaseEnum<Character> {

	    EXPOSED('E'), HIDDEN('H');
	    
	    public final Character value;
	    
	    private SaleStatus(Character value) {
	        this.value = value;
	    }
	    
	    public static SaleStatus valueOf(Character value) {
	        if (value == null) {
	            throw BaseEnum.npeException(SaleStatus.class);
	        }
	        for (SaleStatus status : SaleStatus.values()) {
	            if (value.equals(status.value)) {
	                return status;
	            }
	        }
	        throw BaseEnum.unknownValueException(SaleStatus.class, value);
	    }
	    
	    @Override
	    public Character getValue() {
	        return value;
	    }
	}
}
