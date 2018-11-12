package org.dragberry.eshop.dal.repo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ArrayUtils;
import org.dragberry.eshop.dal.entity.Product;
import org.dragberry.eshop.dal.entity.ProductArticle_;
import org.dragberry.eshop.dal.entity.Product_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

	public static class ProductOrderItemSpecification implements Specification<Product> {

		private static final long serialVersionUID = -3306443889327629630L;

		private final Map<String, String[]> searchParams;
		
		public ProductOrderItemSpecification(Map<String, String[]> searchParams) {
			this.searchParams = searchParams;
		}
		
		@Override
		public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
			root.join(Product_.productArticle);
			root.join(Product_.options, JoinType.LEFT);
			String[] queryStr = searchParams.get("query");
			if (ArrayUtils.isNotEmpty(queryStr) && !queryStr[0].isEmpty()) {
				List<Predicate> predicates = Arrays.stream(queryStr[0].toUpperCase().split("\\s+"))
					.map(str -> "%" + str +  "%")
					.flatMap(param -> {
						return Stream.of(
								cb.like(cb.upper(root.get(Product_.productArticle).get(ProductArticle_.article)), param),
								cb.like(cb.upper(root.get(Product_.productArticle).get(ProductArticle_.title)), param),
								cb.like(cb.upper(root.get(Product_.productArticle).get(ProductArticle_.description)), param));
						}).collect(Collectors.toList());
					return cb.or(predicates.toArray(new Predicate[predicates.size()]));
			}
			return null;
		}
		
	}
}
