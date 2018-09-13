package org.dragberry.eshop.specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.dragberry.eshop.dal.entity.ProductArticle;
import org.springframework.data.jpa.domain.Specification;

public class ProductArticleSpecification implements Specification<ProductArticle> {

    private static final long serialVersionUID = 1408556421987136103L;

    private final String categoryReference;
    
    private final Map<String, String[]> searchParams;
    
    public ProductArticleSpecification(String categoryReference, Map<String, String[]> searchParams) {
        this.categoryReference = categoryReference;
        this.searchParams = searchParams;
    }
    
    @Override
    public Predicate toPredicate(Root<ProductArticle> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        var productRoot = root.join("products");
        var categoryRoot = root.join("categories");
        List<Predicate> where = new ArrayList<>();
        if (StringUtils.isNotBlank(categoryReference)) {
            where.add(cb.equal(categoryRoot.get("reference"), categoryReference));
        }
        searchParams.forEach((name, values) -> {
            if ("from[price]".equals(name) && values.length == 1) {
                try {
                    where.add(cb.greaterThanOrEqualTo(productRoot.get("actualPrice"), new BigDecimal(values[0].replaceAll(" ", ""))));
                } catch (Exception exc) {}
            }
            if ("to[price]".equals(name) && values.length == 1) {
                try {
                    where.add(cb.lessThanOrEqualTo(productRoot.get("actualPrice"), new BigDecimal(values[0].replaceAll(" ", ""))));
                } catch (Exception exc) {}
            }
        });
        query.distinct(true);
        return cb.and(where.toArray(new Predicate[] {}));
    }

}
