package org.dragberry.eshop.dal.entity;

import java.util.stream.Collectors;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.dragberry.eshop.dal.repo.ProductAttributeStringRepository;
import org.dragberry.eshop.model.common.KeyValue;
import org.dragberry.eshop.model.product.Filter;
import org.dragberry.eshop.model.product.ListFilter;
import org.dragberry.eshop.service.filter.FilterTypes;

@Entity
@DiscriminatorValue(FilterTypes.S_ANY)
public class CategoryFilterAnyString extends CategoryFilter<String, ProductAttributeString, ProductAttributeStringRepository> {

	private static final long serialVersionUID = 3389528340207234584L;

	public CategoryFilterAnyString() {
		super(ProductAttributeStringRepository.class, FilterTypes.S_ANY);
	}

	@Override
	protected Filter buildFilter(ProductAttributeStringRepository repo) {
		ListFilter filter = new ListFilter();
		filter.setId(getAttribute(name));
		filter.setName(name);
		filter.setAttributes(repo.findByNameAndCategory(name, category).stream().map(pa -> new KeyValue(pa, pa)).collect(Collectors.toList()));
		return filter;
	}

}
