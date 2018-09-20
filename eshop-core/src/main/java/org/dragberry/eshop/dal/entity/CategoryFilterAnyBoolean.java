package org.dragberry.eshop.dal.entity;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.dragberry.eshop.dal.repo.ProductAttributeBooleanRepository;
import org.dragberry.eshop.model.common.KeyValue;
import org.dragberry.eshop.model.product.Filter;
import org.dragberry.eshop.model.product.ListFilter;
import org.dragberry.eshop.service.filter.FilterTypes;

@Entity
@DiscriminatorValue(FilterTypes.B_ANY)
public class CategoryFilterAnyBoolean extends CategoryFilter<Boolean, ProductAttributeBoolean, ProductAttributeBooleanRepository> {

	private static final long serialVersionUID = 3389528340207234584L;

	public CategoryFilterAnyBoolean() {
		super(ProductAttributeBooleanRepository.class, FilterTypes.B_ANY);
	}

	@Override
	protected Filter buildFilter(ProductAttributeBooleanRepository repo) {
		ListFilter filter = new ListFilter();
		filter.setId(getAttribute(name));
		filter.setName(name);
		filter.setAttributes(
		        Stream.concat(
		                Stream.of(new KeyValue("msg.common.false", Boolean.FALSE)),
		                repo.findByNameAndCategory(name, category).stream().map(pa -> new KeyValue(pa, pa))
		        ).collect(Collectors.toList()));
		return filter;
	}

}
