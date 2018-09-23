package org.dragberry.eshop.dal.repo;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.dragberry.eshop.dal.dto.ProductListItemDTO;
import org.dragberry.eshop.dal.entity.Product;
import org.dragberry.eshop.dal.entity.ProductArticle;
import org.dragberry.eshop.dal.entity.ProductAttributeBoolean;
import org.dragberry.eshop.dal.entity.ProductAttributeList;
import org.dragberry.eshop.dal.entity.ProductAttributeNumeric;
import org.dragberry.eshop.dal.entity.ProductAttributeString;
import org.dragberry.eshop.dal.entity.ProductArticle.SaleStatus;
import org.dragberry.eshop.service.filter.FilterTypes;
import org.springframework.data.domain.Sort.Direction;

public class ProductArticleSearchRepositoryImpl implements ProductArticleSearchRepository {

    private static final Pattern OPTION_PATTERN = Pattern.compile("option\\[(.*?)\\]$");
    
    private static final Pattern SORT_PATTERN = Pattern.compile("(.*?)\\[(.*?)\\]$");
    
    private static final String SORT_PARAM = "sort";

    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile(MessageFormat.format("attribute\\[(.*?)\\]\\[({0})\\]$", StringUtils.join(new String[] {
            FilterTypes.B_ALL,
            FilterTypes.B_ANY,
            FilterTypes.L_ALL,
            FilterTypes.S_ANY,
            FilterTypes.FROM,
            FilterTypes.TO }, "|")));
	
	@PersistenceContext
	private EntityManager em;
	
	public List<ProductListItemDTO> search(String categoryReference, Map<String, String[]> searchParams){
		return new ProductSearchQuery().search(categoryReference, searchParams);
	}
	
	private class ProductSearchQuery {
		CriteriaBuilder cb;
		CriteriaQuery<ProductListItemDTO> query;
		Root<ProductArticle> root;
		Join<?, ?> categoryRoot;
		Join<?, ?>  productRoot;
		List<Predicate> where;
		
		ProductSearchQuery() {
			cb = em.getCriteriaBuilder();
			query = cb.createQuery(ProductListItemDTO.class);
			root = query.from(ProductArticle.class);
			categoryRoot = root.join("categories");
			productRoot = root.join("products");
			where = new ArrayList<>();
		}

		List<ProductListItemDTO> search(String categoryReference, Map<String, String[]> searchParams) {
			if (StringUtils.isNotBlank(categoryReference)) {
	            where.add(cb.equal(categoryRoot.get("reference"), categoryReference));
	        }
			where.add(cb.equal(root.get("saleStatus"), SaleStatus.EXPOSED));
	        for (Entry<String, String[]> entry : searchParams.entrySet()) {
	        	String name = entry.getKey();
	        	String[] values = entry.getValue();
	            if (processPrice(name, values) || processOptions(name, values) || processAttributes(name, values)) {
	            	continue;
	            }
	            if (SORT_PARAM.equals(name)) {
	                sort(values);
	            }
	        }
			query.multiselect(
					root.get("entityKey"),
					root.get("title"),
					root.get("article"),
					root.get("reference"),
					root.get("mainImage").get("entityKey"),
					cb.min(productRoot.get("actualPrice")),
					cb.min(productRoot.get("price")))
			.groupBy(
					root.get("entityKey"))
			.where(where.toArray(new Predicate[where.size()]));
			return em.createQuery(query).getResultList();
		}
		
		private boolean processPrice(String name, String[] values) {
			if ("price[from]".equals(name) && values.length == 1) {
                try { where.add(priceFrom(values)); } catch (Exception exc) {}
                return true;
            } 
            if ("price[to]".equals(name) && values.length == 1) {
                try { where.add(priceTo(values)); } catch (Exception exc) {}
                return true;
            }
            return false;
		}
		
		private boolean processOptions(String name, String[] values) {
			Matcher optionMatcher;
            if ((optionMatcher = OPTION_PATTERN.matcher(name)).find()) {
            	return where.addAll(getOptionExpressions(optionMatcher.group(1), values));
            }
            return false;
		}
		
		private List<Predicate> getOptionExpressions(String optionName, String[] values) {
	    	if (values.length > 0) {
	    		Subquery<Long> sqProduct = query.subquery(Long.class);
	    		Root<Product> fromProduct = sqProduct.from(Product.class);
	    		Join<?, ?> joinOption = fromProduct.join("options"); 
		        return Arrays.asList(cb.exists(sqProduct.select(fromProduct.get("entityKey")).where(
		        		cb.equal(root, fromProduct.get("productArticle")),
		        		cb.equal(joinOption.get("name"), optionName),
		        		joinOption.get("value").in(Arrays.asList(values)))));
			}
	    	return Arrays.asList();
	    }
		
		private boolean processAttributes(String name, String[] values) {
			Matcher attrMatcher;
            if ((attrMatcher = ATTRIBUTE_PATTERN.matcher(name)).find()) {
            	switch (attrMatcher.group(2)) {
				case FilterTypes.L_ALL:
					where.addAll(attributeAll(attrMatcher.group(1), values));
					break;
				case FilterTypes.S_ANY:
					where.addAll(attributeAny(attrMatcher.group(1), values));
					break;
				case FilterTypes.B_ALL:
                    where.addAll(attributeBAll(attrMatcher.group(1), values));
                    break;
				case FilterTypes.B_ANY:
                    where.addAll(attributeBAny(attrMatcher.group(1), values));
                    break;
				case FilterTypes.FROM:
				    where.addAll(attributeFrom(attrMatcher.group(1), values));
					break;
				case FilterTypes.TO:
				    where.addAll(attributeTo(attrMatcher.group(1), values));
                    break;
				default:
					break;
            	}
            	return true;
            }
            return false;
		}
		
		private Predicate priceFrom(String[] values) {
	        return cb.or(
	                cb.and(cb.isNotNull(productRoot.get("actualPrice")),
	                        cb.greaterThanOrEqualTo(productRoot.get("actualPrice"), extractNumberParam(values))),
	                cb.and(cb.isNull(productRoot.get("actualPrice")),
	                        cb.greaterThanOrEqualTo(productRoot.get("price"), extractNumberParam(values))));
	    }

	    private Predicate priceTo(String[] values) {
	        return cb.or(
	                cb.and(cb.isNotNull(productRoot.get("actualPrice")),
	                        cb.lessThanOrEqualTo(productRoot.get("actualPrice"), extractNumberParam(values))),
	                cb.and(cb.isNull(productRoot.get("actualPrice")),
	                        cb.lessThanOrEqualTo(productRoot.get("price"), extractNumberParam(values))));
	    }
	    
	    private List<Predicate> attributeFrom(String attributeName, String[] values) {
	        if (values.length > 0) {
	            try {
	                Subquery<Long> sqPAN = query.subquery(Long.class);
	                Root<ProductAttributeNumeric> fromPAN = sqPAN.from(ProductAttributeNumeric.class);
	                return Arrays.asList(cb.exists(sqPAN.select(fromPAN.get("entityKey")).where(
	                        cb.equal(root, fromPAN.get("productArticle")),
	                        cb.equal(fromPAN.get("name"), attributeName),
	                        cb.greaterThanOrEqualTo(fromPAN.get("value"), extractNumberParam(values)))));
	            } catch (Exception exc) {}
	        }
	        return Arrays.asList();
	    }
	    
	    private List<Predicate> attributeTo(String attributeName, String[] values) {
	        if (values.length > 0) {
	            try {
	                Subquery<Long> sqPAN = query.subquery(Long.class);
	                Root<ProductAttributeNumeric> fromPAN = sqPAN.from(ProductAttributeNumeric.class);
	                return Arrays.asList(cb.exists(sqPAN.select(fromPAN.get("entityKey")).where(
	                        cb.equal(root, fromPAN.get("productArticle")),
	                        cb.equal(fromPAN.get("name"), attributeName),
	                        cb.lessThanOrEqualTo(fromPAN.get("value"), extractNumberParam(values)))));
	            } catch (Exception exc) {}
	        }
	        return Arrays.asList();
	    }

	    private BigDecimal extractNumberParam(String[] values) {
	        return new BigDecimal(values[0].replaceAll(" ", ""));
	    }
	    
	    private List<Predicate> attributeAny(String attributeName, String[] values) {
			if (values.length > 0) {
		    	Subquery<Long> sqPAS = query.subquery(Long.class);
		        Root<ProductAttributeString> fromPAS = sqPAS.from(ProductAttributeString.class);
		        return Arrays.asList(cb.exists(sqPAS.select(fromPAS.get("entityKey")).where(
		        		cb.equal(root, fromPAS.get("productArticle")),
		        		cb.equal(fromPAS.get("name"), attributeName),
		        		fromPAS.get("value").in(Arrays.asList(values)))));
			}
	    	return Arrays.asList();
	    }
	    
	    private List<Predicate> attributeAll(String attributeName, String[] values) {
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
	  	        
	  	        return Arrays.asList(cb.exists(sqPAL.select(fromPAL.get("entityKey")).where(
	  	        		cb.equal(root, fromPAL.get("productArticle")),
	  	        		cb.equal(fromPAL.get("name"), attributeName),
	  	        		cb.and(restrictions))));
	  		}
	      	return Arrays.asList();
	      }
	    
	    private List<Predicate> attributeBAny(String attributeName, String[] values) {
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
	           return Arrays.asList(cb.or(predicates));
	        }
	        return Arrays.asList();
	    }
	    
	    private List<Predicate> attributeBAll(String attributeName, String[] values) {
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
	    	return Arrays.asList();
	    }
		
		private void sort(String[] values) {
	        if (values.length > 0) {
	            Matcher orderMatcher;
	            if ((orderMatcher = SORT_PATTERN.matcher(values[0])).find()) {
	            	Direction.fromOptionalString(orderMatcher.group(2)).ifPresent(direction -> {
                        switch (direction) {
                        case ASC:
                        	switch (orderMatcher.group(1)) {
                        	case "price":	
                        		query.orderBy(cb.asc(cb.min(productRoot.get("actualPrice"))));
                        		break;
                        	default:
                        		break;
                        	}
                            break;
                        case DESC:
                        	switch (orderMatcher.group(1)) {
                        	case "popular":
                        		query.orderBy(cb.desc(cb.count(productRoot.join("orderItems", JoinType.LEFT).get("quantity"))));
                        		break;
                        	case "price":	
                        		query.orderBy(cb.desc(cb.min(productRoot.get("actualPrice"))));
                        		break;
                        	case "date":
                        		query.orderBy(cb.desc(root.get("modifiedDate")));
                        	default:
                        		break;
                        	}
                            break;
                        default:
                            break;
                        }
                    });
	            }
	        }
	        if (query.getOrderList().isEmpty()) {
	        	query.orderBy(cb.desc(cb.count(productRoot.join("orderItems", JoinType.LEFT).get("quantity"))));
	        }
	    }	
		
	}
}
