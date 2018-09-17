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
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.dragberry.eshop.dal.entity.ProductArticle;
import org.dragberry.eshop.dal.entity.ProductAttributeBoolean;
import org.springframework.data.jpa.domain.Specification;

public class ProductArticleSpecification implements Specification<ProductArticle> {

    private static final long serialVersionUID = 1408556421987136103L;
    
    private static final Pattern OPTION_PATTERN = Pattern.compile("option\\[(.*?)\\]$");

    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("attribute\\[(.*?)\\]$");
    
    private final String categoryReference;
    
    private final Map<String, String[]> searchParams;
    
    private Join<?, ?> categoryRoot;
    
    private Join<?, ?> productRoot;
    
    private Join<?, ?> optionRoot;
    
    public ProductArticleSpecification(String categoryReference, Map<String, String[]> searchParams) {
        this.categoryReference = categoryReference;
        this.searchParams = searchParams;
    }
    
    @Override
    public Predicate toPredicate(Root<ProductArticle> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
    	categoryRoot = root.join("categories");
    	productRoot = root.join("products");
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
            
            Matcher optionMatcher;
            if ((optionMatcher = OPTION_PATTERN.matcher(name)).find()) {
            	where.addAll(getOptionExpressions(optionMatcher.group(1), values, root, query, cb));
            	continue;
            }
            Matcher attrMatcher;
            if ((attrMatcher = ATTRIBUTE_PATTERN.matcher(name)).find()) {
            	where.addAll(getAttributeExpressions(attrMatcher.group(1), values, root, query, cb));
            	continue;
            }
        }
        query.distinct(true);
        return cb.and(where.toArray(new Predicate[where.size()]));
    }
    
    private List<Predicate> getOptionExpressions(String optionName, String[] values, Root<ProductArticle> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		if (values.length > 0) {
			if (optionRoot == null) {
				optionRoot = productRoot.join("options");
			}
			return List.of(
					cb.equal(optionRoot.get("name"), optionName),
					optionRoot.get("value").in(Arrays.asList(values)));
		}
		return List.of();
    }
    
    private List<Predicate> getAttributeExpressions(String attributeName, String[] values, Root<ProductArticle> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		if (values.length > 0) {
			List<Predicate> predicates = new ArrayList<>(values.length);
			for (String value : values) {
			    if (Boolean.FALSE.toString().equalsIgnoreCase(value) || (Boolean.TRUE.toString().equalsIgnoreCase(value))) {
			    	Subquery<Long> sqPAB = query.subquery(Long.class);
			        Root<ProductAttributeBoolean> fromPAB = sqPAB.from(ProductAttributeBoolean.class);
			        predicates.add(cb.exists(sqPAB.select(fromPAB.get("entityKey")).where(
			        		cb.equal(root, fromPAB.get("productArticle")),
			        		cb.equal(fromPAB.get("name"), attributeName),
			        		cb.equal(fromPAB.get("value"), Boolean.parseBoolean(value)))));
			    } else {
			    	Subquery<Long> sqPAB = query.subquery(Long.class);
			        Root<ProductAttributeBoolean> fromPAB = sqPAB.from(ProductAttributeBoolean.class);
			        predicates.add(cb.exists(sqPAB.select(fromPAB.get("entityKey")).where(
			        		cb.equal(root, fromPAB.get("productArticle")),
			        		cb.equal(fromPAB.get("name"), attributeName),
			        		cb.equal(fromPAB.get("value"), true),
			        		cb.and(cb.equal(fromPAB.get("description"), value)))));
			    }
			}
			return predicates;
		}
    	return List.of();
    }

}
