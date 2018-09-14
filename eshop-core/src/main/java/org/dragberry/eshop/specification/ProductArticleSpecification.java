package org.dragberry.eshop.specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.dragberry.eshop.dal.entity.ProductArticle;
import org.springframework.data.jpa.domain.Specification;

public class ProductArticleSpecification implements Specification<ProductArticle> {

    private static final long serialVersionUID = 1408556421987136103L;
    
    private static final Pattern OPTION_PATTERN = Pattern.compile("option\\[(.*?)\\]$");

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
        Join<?, ?> optionRoot = null;
        List<Predicate> where = new ArrayList<>();
        if (StringUtils.isNotBlank(categoryReference)) {
            where.add(cb.equal(categoryRoot.get("reference"), categoryReference));
        }
        for (Entry<String, String[]> entry : searchParams.entrySet()) {
        	var name = entry.getKey();
        	var values = entry.getValue();
            if ("from[price]".equals(name) && values.length == 1) {
                try {
                    where.add(cb.or(
                            cb.and(cb.isNotNull(productRoot.get("actualPrice")),
                                    cb.greaterThanOrEqualTo(productRoot.get("actualPrice"), new BigDecimal(values[0].replaceAll(" ", "")))),
                            cb.and(cb.isNull(productRoot.get("actualPrice")),
                                    cb.greaterThanOrEqualTo(productRoot.get("price"), new BigDecimal(values[0].replaceAll(" ", ""))))));
                } catch (Exception exc) {}
                continue;
            } 
            if ("to[price]".equals(name) && values.length == 1) {
                try {
                    where.add(cb.or(
                            cb.and(cb.isNotNull(productRoot.get("actualPrice")),
                                    cb.lessThanOrEqualTo(productRoot.get("actualPrice"), new BigDecimal(values[0].replaceAll(" ", "")))),
                            cb.and(cb.isNull(productRoot.get("actualPrice")),
                                    cb.lessThanOrEqualTo(productRoot.get("price"), new BigDecimal(values[0].replaceAll(" ", ""))))));
                } catch (Exception exc) {}
                continue;
            }
            Matcher optionMatcher = OPTION_PATTERN.matcher(name);
			if (optionMatcher.find() && values.length > 0) {
				if (optionRoot == null) {
					optionRoot = productRoot.join("options");
				}
            	where.add(cb.equal(optionRoot.get("name"), optionMatcher.group(1)));
            	where.add(optionRoot.get("value").in(Arrays.asList(values)));
            }
        }
        query.distinct(true);
        return cb.and(where.toArray(new Predicate[] {}));
    }

}
