package org.dragberry.eshop.dal.entity;

import java.text.MessageFormat;
import java.util.stream.Collectors;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.dragberry.eshop.dal.repo.ProductAttributeBooleanRepository;
import org.dragberry.eshop.model.common.KeyValue;
import org.dragberry.eshop.model.product.Filter;
import org.dragberry.eshop.model.product.GroupFilter;

@Entity
@DiscriminatorValue("bAll")
public class CategoryFilterAllBoolean extends CategoryFilter<Boolean, ProductAttributeBoolean, ProductAttributeBooleanRepository> {

	private static final long serialVersionUID = 3389528340207234584L;

	public CategoryFilterAllBoolean() {
		super(ProductAttributeBooleanRepository.class);
	}

	@Override
	protected Filter buildFilter(ProductAttributeBooleanRepository repo) {
		GroupFilter filter = new GroupFilter();
		filter.setId(name);
		filter.setName(name);
		filter.setAttributes(repo.findByGroupAndCategory(name, category).stream()
				.map(pa -> new KeyValue(pa, MessageFormat.format("attribute[{0}][is]", pa))).collect(Collectors.toList()));
		return filter;
	}

}
