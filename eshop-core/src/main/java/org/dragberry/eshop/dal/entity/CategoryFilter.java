package org.dragberry.eshop.dal.entity;

import java.text.MessageFormat;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.dragberry.eshop.dal.repo.ProductAttributeRepository;
import org.dragberry.eshop.model.product.Filter;
import org.springframework.context.ApplicationContext;

import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE")
@Table(name = "CATEGORY_FILTER")
@TableGenerator(
		name = "CATEGORY_FILTER_GEN", 
		table = "GENERATOR",
		pkColumnName = "GEN_NAME", 
		pkColumnValue = "CATEGORY_FILTER_GEN",
		valueColumnName = "GEN_VALUE",
		initialValue = 1000,
		allocationSize = 1)
@Getter
@Setter
public abstract class CategoryFilter<V, A extends ProductAttribute<V>, R extends ProductAttributeRepository<V, A>> extends BaseEntity {
	
	private static final long serialVersionUID = 1261949029541186996L;
	
	private static final String ATTR_TEMPLATE = "attribute[{0}][{1}]";

	@Id
	@Column(name = "CATEGORY_FILTER_KEY")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CATEGORY_FILTER_GEN")
	private Long entityKey;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_KEY", referencedColumnName = "CATEGORY_KEY")
	protected Category category;
	
	@Column(name = "NAME")
	protected String name;
	
	@Column(name = "`ORDER`")
	private Integer order;
	
	@Transient
	private final Class<R> repoClass;
	
	@Transient
	private final String operator;
	
	protected String getAttribute(String value) {
	    return MessageFormat.format(ATTR_TEMPLATE, value, operator);
	}

	public CategoryFilter(Class<R> repoClass, String operator) {
		this.repoClass = repoClass;
		this.operator = operator;
	}
	
	public Filter buildFilter(ApplicationContext appContext) {
		return buildFilter(appContext.getBean(repoClass));
	}

	protected abstract Filter buildFilter(R repo);
}
