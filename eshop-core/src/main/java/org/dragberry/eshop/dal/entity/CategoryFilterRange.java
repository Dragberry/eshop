package org.dragberry.eshop.dal.entity;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.dragberry.eshop.dal.repo.ProductAttributeNumericRepository;
import org.dragberry.eshop.model.product.Filter;
import org.dragberry.eshop.model.product.RangeFilter;
import org.dragberry.eshop.service.filter.FilterTypes;

@Entity
@DiscriminatorValue(FilterTypes.RANGE)
public class CategoryFilterRange extends CategoryFilter<BigDecimal, ProductAttributeNumeric, ProductAttributeNumericRepository> {

	private static final long serialVersionUID = 3389528340207234584L;

	public CategoryFilterRange() {
		super(ProductAttributeNumericRepository.class, FilterTypes.RANGE);
	}

	@Override
	protected Filter buildFilter(ProductAttributeNumericRepository repo) {
		RangeFilter filter = new RangeFilter();
		List<String> units = repo.getUnits(name, category);
		filter.setId(name);
		filter.setFromId(getAttribute(name, FilterTypes.FROM));
		filter.setToId(getAttribute(name, FilterTypes.TO));
		filter.setName(units.size() == 1 ? MessageFormat.format("{0}, {1}", name, units.get(0)) : name);
		return filter;
	}

}
