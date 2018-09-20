package org.dragberry.eshop.specification;

import java.math.BigDecimal;
import java.text.MessageFormat;
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
import org.dragberry.eshop.dal.entity.ProductAttributeList;
import org.dragberry.eshop.dal.entity.ProductAttributeNumeric;
import org.dragberry.eshop.dal.entity.ProductAttributeString;
import org.dragberry.eshop.service.filter.FilterTypes;
import org.springframework.data.jpa.domain.Specification;

public class ProductArticleSpecification implements Specification<ProductArticle> {

    private static final long serialVersionUID = 1408556421987136103L;
    
    private static final Pattern OPTION_PATTERN = Pattern.compile("option\\[(.*?)\\]$");

    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile(MessageFormat.format("attribute\\[(.*?)\\]\\[({0})\\]$", StringUtils.join(new String[] {
            FilterTypes.B_ALL,
            FilterTypes.B_ANY,
            FilterTypes.L_ALL,
            FilterTypes.S_ANY,
            FilterTypes.FROM,
            FilterTypes.TO }, "|")));
    
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
            if ("price[from]".equals(name) && values.length == 1) {
                try {
                    where.add(cb.or(
                            cb.and(cb.isNotNull(productRoot.get("actualPrice")),
                                    cb.greaterThanOrEqualTo(productRoot.get("actualPrice"), extractNumberParam(values))),
                            cb.and(cb.isNull(productRoot.get("actualPrice")),
                                    cb.greaterThanOrEqualTo(productRoot.get("price"), extractNumberParam(values)))));
                } catch (Exception exc) {}
                continue;
            } 
            if ("price[to]".equals(name) && values.length == 1) {
                try {
                    where.add(cb.or(
                            cb.and(cb.isNotNull(productRoot.get("actualPrice")),
                                    cb.lessThanOrEqualTo(productRoot.get("actualPrice"), extractNumberParam(values))),
                            cb.and(cb.isNull(productRoot.get("actualPrice")),
                                    cb.lessThanOrEqualTo(productRoot.get("price"), extractNumberParam(values)))));
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
            	switch (attrMatcher.group(2)) {
				case FilterTypes.L_ALL:
					where.addAll(attributeAll(attrMatcher.group(1), values, root, query, cb));
					break;
				case FilterTypes.S_ANY:
					where.addAll(attributeAny(attrMatcher.group(1), values, root, query, cb));
					break;
				case FilterTypes.B_ALL:
                    where.addAll(attributeBAll(attrMatcher.group(1), values, root, query, cb));
                    break;
				case FilterTypes.B_ANY:
                    where.addAll(attributeBAny(attrMatcher.group(1), values, root, query, cb));
                    break;
				case FilterTypes.FROM:
				    where.addAll(attributeFrom(attrMatcher.group(1), values, root, query, cb));
					break;
				case FilterTypes.TO:
				    where.addAll(attributeTo(attrMatcher.group(1), values, root, query, cb));
                    break;
				default:
					break;
            	}
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
    
    private List<Predicate> attributeFrom(String attributeName, String[] values, Root<ProductArticle> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (values.length > 0) {
            try {
                Subquery<Long> sqPAN = query.subquery(Long.class);
                Root<ProductAttributeNumeric> fromPAN = sqPAN.from(ProductAttributeNumeric.class);
                return List.of(cb.exists(sqPAN.select(fromPAN.get("entityKey")).where(
                        cb.equal(root, fromPAN.get("productArticle")),
                        cb.equal(fromPAN.get("name"), attributeName),
                        cb.greaterThanOrEqualTo(fromPAN.get("value"), extractNumberParam(values)))));
            } catch (Exception exc) {}
        }
        return List.of();
    }
    
    private List<Predicate> attributeTo(String attributeName, String[] values, Root<ProductArticle> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (values.length > 0) {
            try {
                Subquery<Long> sqPAN = query.subquery(Long.class);
                Root<ProductAttributeNumeric> fromPAN = sqPAN.from(ProductAttributeNumeric.class);
                return List.of(cb.exists(sqPAN.select(fromPAN.get("entityKey")).where(
                        cb.equal(root, fromPAN.get("productArticle")),
                        cb.equal(fromPAN.get("name"), attributeName),
                        cb.lessThanOrEqualTo(fromPAN.get("value"), extractNumberParam(values)))));
            } catch (Exception exc) {}
        }
        return List.of();
    }

    private static BigDecimal extractNumberParam(String[] values) {
        return new BigDecimal(values[0].replaceAll(" ", ""));
    }
    
    private List<Predicate> attributeAny(String attributeName, String[] values, Root<ProductArticle> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		if (values.length > 0) {
	    	Subquery<Long> sqPAS = query.subquery(Long.class);
	        Root<ProductAttributeString> fromPAS = sqPAS.from(ProductAttributeString.class);
	        return List.of(cb.exists(sqPAS.select(fromPAS.get("entityKey")).where(
	        		cb.equal(root, fromPAS.get("productArticle")),
	        		cb.equal(fromPAS.get("name"), attributeName),
	        		fromPAS.get("value").in(List.of(values)))));
		}
    	return List.of();
    }
    
    private List<Predicate> attributeAll(String attributeName, String[] values, Root<ProductArticle> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
  		if (values.length > 0) {
  	    	Subquery<Long> sqPAL = query.subquery(Long.class);
  	        Root<ProductAttributeList> fromPAL = sqPAL.from(ProductAttributeList.class);
  	        
  	        Predicate[] restrictions = new Predicate[values.length];
  	        for (int i = 0; i < values.length; i++) {
  	        	Subquery<String> sqPALV = sqPAL.subquery(String.class);
  	        	Root<ProductAttributeList> fromPALV = sqPALV.from(ProductAttributeList.class);
  	        	Join<Object, String> joinValues = fromPALV.join("value");
  	        	restrictions[i] = cb.exists(sqPALV.select(joinValues)
  	        	        .where(cb.equal(fromPAL, fromPALV), cb.equal(joinValues, values[i])));;
  	        }
  	        
  	        return List.of(cb.exists(sqPAL.select(fromPAL.get("entityKey")).where(
  	        		cb.equal(root, fromPAL.get("productArticle")),
  	        		cb.equal(fromPAL.get("name"), attributeName),
  	        		cb.and(restrictions))));
  		}
      	return List.of();
      }
    
    private List<Predicate> attributeBAny(String attributeName, String[] values, Root<ProductArticle> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (values.length > 0) {
            List<String> trueValues = new ArrayList<>(values.length);
            boolean falsePresent = false;
            for (String value : values) {
                if (Boolean.FALSE.toString().equalsIgnoreCase(value)) {
                    falsePresent = true;
                } else {
                    trueValues.add(value);
                }
            }
           Predicate[] predicates = new Predicate[(falsePresent ? 1 : 0) + (trueValues.isEmpty() ? 0 : 1)];
           int index = 0;
           if (falsePresent) {
               Subquery<Long> sqPAB = query.subquery(Long.class);
               Root<ProductAttributeBoolean> fromPAB = sqPAB.from(ProductAttributeBoolean.class);
               predicates[index] = cb.exists(sqPAB.select(fromPAB.get("entityKey")).where(
                       cb.equal(root, fromPAB.get("productArticle")),
                       cb.equal(fromPAB.get("name"), attributeName),
                       cb.equal(fromPAB.get("value"), false)));
               index++;
           }
           if (!trueValues.isEmpty()) {
               Subquery<Long> sqPAB = query.subquery(Long.class);
               Root<ProductAttributeBoolean> fromPAB = sqPAB.from(ProductAttributeBoolean.class);
               predicates[index] = cb.exists(sqPAB.select(fromPAB.get("entityKey")).where(
                       cb.equal(root, fromPAB.get("productArticle")),
                       cb.equal(fromPAB.get("name"), attributeName),
                       cb.equal(fromPAB.get("value"), true),
                       fromPAB.get("description").in(trueValues)));
           }
           return List.of(cb.or(predicates));
        }
        return List.of();
    }
    
    private List<Predicate> attributeBAll(String attributeName, String[] values, Root<ProductArticle> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
			    }
			}
			return predicates;
		}
    	return List.of();
    }

}
