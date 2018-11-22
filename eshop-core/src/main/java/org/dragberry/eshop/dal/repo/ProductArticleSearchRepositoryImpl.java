package org.dragberry.eshop.dal.repo;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.dragberry.eshop.dal.dto.ProductArticleListItemDTO;
import org.dragberry.eshop.dal.dto.ProductListItemDTO;
import org.dragberry.eshop.dal.entity.Category;
import org.dragberry.eshop.dal.entity.Category_;
import org.dragberry.eshop.dal.entity.File;
import org.dragberry.eshop.dal.entity.File_;
import org.dragberry.eshop.dal.entity.Product;
import org.dragberry.eshop.dal.entity.ProductArticle;
import org.dragberry.eshop.dal.entity.ProductAttributeBoolean;
import org.dragberry.eshop.dal.entity.ProductAttributeList;
import org.dragberry.eshop.dal.entity.ProductAttributeNumeric;
import org.dragberry.eshop.dal.entity.ProductAttributeString;
import org.dragberry.eshop.dal.entity.Product_;
import org.dragberry.eshop.dal.entity.ProductArticle.SaleStatus;
import org.dragberry.eshop.dal.entity.ProductArticle_;
import org.dragberry.eshop.dal.sort.Roots;
import org.dragberry.eshop.dal.sort.SortConfig;
import org.dragberry.eshop.dal.sort.SortContext;
import org.dragberry.eshop.dal.sort.SortFunction;
import org.dragberry.eshop.service.filter.FilterTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import lombok.AllArgsConstructor;

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
	
	@Override
	public List<ProductListItemDTO> quickSearch(String query, Map<String, String[]> searchParams) {
		if (!GenericValidator.minLength(query, 2)) {
			return Collections.emptyList();
		}
		return new ProductQuickSearchQuery(query).search(searchParams);
	}
	
	public List<ProductListItemDTO> search(String categoryReference, Map<String, String[]> searchParams){
		return new ProductSearchQuery(categoryReference).search(searchParams);
	}
	
	private class ProductQuickSearchQuery extends AbstractProductSearchQuery {
        
        private String query;
        
        ProductQuickSearchQuery(String query) {
            this.query = query;
        }
        
        @Override
        protected Predicate getPredicate() {
            return cb.or(where.toArray(new Predicate[where.size()]));
        }
        
        @Override
        protected void addPredicates(Map<String, String[]> searchParams) {
            Arrays.stream(query.toUpperCase().split("\\s+")).map(str -> "%" + str +  "%").forEach(str -> {
                where.add(cb.like(cb.upper(root.get("article")), str));
                where.add(cb.like(cb.upper(root.get("title")), str));
                where.add(cb.like(cb.upper(root.get("description")), str));
            });
        }
        
    }   
	
	private class ProductSearchQuery extends AbstractProductSearchQuery {
	    
	    private String categoryReference;
	    
	    ProductSearchQuery(String categoryRefence) {
	        this.categoryReference = categoryRefence;
	    }
	    
	    @Override
	    protected Predicate getPredicate() {
	        return cb.and(where.toArray(new Predicate[where.size()]));
	    }
	    
	    @Override
	    protected void addPredicates(Map<String, String[]> searchParams) {
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
            }
	    }
	    
	}
	
	private abstract class AbstractProductSearchQuery {
		CriteriaBuilder cb;
		CriteriaQuery<ProductListItemDTO> query;
		Root<ProductArticle> root;
		Join<ProductArticle, File> mainImageRoot;
		Join<?, ?> mainCategoryRoot;
		Join<?, ?> categoryRoot;
		Join<?, ?>  productRoot;
		Join<?, ?>  productCommentRoot;
		List<Predicate> where;
		
		AbstractProductSearchQuery() {
			cb = em.getCriteriaBuilder();
			query = cb.createQuery(ProductListItemDTO.class);
			root = query.from(ProductArticle.class);
			mainCategoryRoot = root.join("category");
			categoryRoot = root.join("categories");
			productRoot = root.join("products");
			productCommentRoot = root.join("comments", JoinType.LEFT);
			mainImageRoot = root.join(ProductArticle_.mainImage, JoinType.LEFT);
			where = new ArrayList<>();
		}

		protected abstract Predicate getPredicate();
		
		protected abstract void addPredicates(Map<String, String[]> searchParams);
		
		protected void addSorting(Map<String, String[]> searchParams) {
		    String[] values = searchParams.get(SORT_PARAM);
		    if (values != null) {
		        sort(values);
		    }
		    if (query.getOrderList().isEmpty()) {
                query.orderBy(cb.desc(cb.count(productRoot.join("orderItems", JoinType.LEFT).get("quantity"))));
            }
		}
		
		List<ProductListItemDTO> search(Map<String, String[]> searchParams) {
			Objects.requireNonNull(searchParams);
			addPredicates(searchParams);
			addSorting(searchParams);
			query.multiselect(
					root.get("entityKey"),
					root.get("title"),
					root.get("article"),
					root.get("reference"),
					root.get("description"),
					cb.min(productRoot.get("actualPrice")),
					cb.min(productRoot.get("price")),
					cb.countDistinct(productCommentRoot),
					cb.avg(productCommentRoot.get("mark")),
					mainCategoryRoot.get("entityKey"),
					mainCategoryRoot.get("name"),
					mainCategoryRoot.get("reference"),
					mainImageRoot.get(File_.path))
			.groupBy(
					root.get("entityKey"))
			.where(getPredicate());
			return em.createQuery(query).getResultList();
		}
		
		protected boolean processPrice(String name, String[] values) {
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
		
		protected boolean processOptions(String name, String[] values) {
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
		
		protected boolean processAttributes(String name, String[] values) {
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
                        	case "rated":
                        		query.orderBy(cb.desc(cb.avg(productCommentRoot.get("mark"))));
                        		break;
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
	    }	
	}
	
	private static final String ID = "id";
	private static final String ARTICLE = "article";
	private static final String TITLE = "title";
	private static final String PRICE = "price";
	private static final String ACTUAL_PRICE = "actualPrice";
	private static final String CATEGORY_ID = "categoryId";
	private static final String SEARCH_QUERY = "searchQuery";
	
	@AllArgsConstructor(staticName = "of")
    private static class ProductArticleRoots implements Roots {
        final Root<ProductArticle> productArticle;
        final Join<ProductArticle, Product> product;
        final Join<ProductArticle, File> mainImage;
        final Join<ProductArticle, Category> mainCategory;
        final Join<ProductArticle, Category> categories;
    }
	
	private static final SortConfig<ProductArticleRoots> SORT_CONFIG = new SortConfig<>() {
        
        private final Map<String, SortFunction<ProductArticleRoots>> config = new HashMap<>();
        {
            config.put(ID, SortFunction.of(ctx -> ctx.roots.productArticle.get(ProductArticle_.entityKey)));
            config.put(ARTICLE, SortFunction.of(ctx -> ctx.roots.productArticle.get(ProductArticle_.article)));
            config.put(TITLE, SortFunction.of(ctx -> ctx.roots.productArticle.get(ProductArticle_.title)));
            config.put(PRICE, SortFunction.of(ctx -> ctx.cb.min(ctx.roots.product.get(Product_.price))));
            config.put(ACTUAL_PRICE, SortFunction.of(ctx -> ctx.cb.min(ctx.roots.product.get(Product_.actualPrice))));
        }
        
        @Override
        public SortFunction<ProductArticleRoots> get(String param) {
            return config.get(param);
        }
        
        @Override
        public SortFunction<ProductArticleRoots> getDefault() {
            return SortFunction.of(ctx -> ctx.roots.productArticle.get(ProductArticle_.entityKey), Direction.DESC);
        }
    };
	
	/**
	 * Search product articles for CMS product list page
	 */
	@Override
	public Page<ProductArticleListItemDTO> search(PageRequest pageRequest, Map<String, String[]> searchParams) {
	    return new ProductArticleSearchQuery().search(pageRequest, searchParams);
	}
	
	private class ProductArticleSearchQuery extends AbstractSearchQuery<ProductArticleListItemDTO, ProductArticleRoots> {

        public ProductArticleSearchQuery() {
            super(ProductArticleListItemDTO.class, em);
        }

        @Override
        protected ProductArticleRoots getRoots(CriteriaQuery<?> query) {
            Root<ProductArticle> root = query.from(ProductArticle.class);
            return ProductArticleRoots.of(root,
                    root.join(ProductArticle_.products),
                    root.join(ProductArticle_.mainImage),
                    root.join(ProductArticle_.category, JoinType.LEFT),
                    root.join(ProductArticle_.categories, JoinType.LEFT));
        }

        @Override
        protected List<Selection<?>> getSelectionList() {
            return List.of(
                    roots.productArticle.get(ProductArticle_.entityKey),
                    roots.productArticle.get(ProductArticle_.article),
                    roots.productArticle.get(ProductArticle_.title),
                    cb.min(roots.product.get(Product_.price)),
                    cb.min(roots.product.get(Product_.actualPrice)),
                    cb.count(roots.product.get(Product_.entityKey)),
                    roots.mainImage.get(File_.path));
        }

        @Override
        protected Expression<?> getCountExpression() {
            return countRoots.productArticle.get(ProductArticle_.entityKey);
        }

        @Override
        protected void where(List<Predicate> predicates, Map<String, String[]> searchParams, ProductArticleRoots roots) {
            String[] categoryIds = searchParams.get(CATEGORY_ID);
            if (ArrayUtils.isNotEmpty(categoryIds)) {
                Long categoryId = Long.valueOf(categoryIds[0]);
                predicates.add(cb.or(
                        cb.equal(roots.mainCategory.get(Category_.entityKey), categoryId),
                        cb.equal(roots.categories.get(Category_.entityKey), categoryId)));
            }
            predicates.addAll(numericRange(PRICE, roots.product.get(Product_.price), searchParams));
            predicates.addAll(numericRange(ACTUAL_PRICE, roots.product.get(Product_.actualPrice), searchParams));
            predicates.addAll(likeFromString(SEARCH_QUERY, searchParams, List.of(
                    roots.productArticle.get(ProductArticle_.article),
                    roots.productArticle.get(ProductArticle_.title),
                    roots.productArticle.get(ProductArticle_.description))));
        }
        
        @Override
        protected Optional<List<Expression<?>>> groupBy(ProductArticleRoots roots) {
            return Optional.of(List.of(roots.productArticle.get(ProductArticle_.entityKey)));
        }

        @Override
        protected SortConfig<ProductArticleRoots> getSortConfig() {
            return SORT_CONFIG;
        }

        @Override
        protected SortContext<ProductArticleRoots> getSortContext() {
            return SortContext.of(cb, roots);
        }

	}
}
