package org.dragberry.eshop.controller.product;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.dragberry.eshop.common.ResultTO;
import org.dragberry.eshop.common.Results;
import org.dragberry.eshop.controller.exception.BadRequestException;
import org.dragberry.eshop.controller.exception.ResourceNotFoundException;
import org.dragberry.eshop.model.comment.ProductCommentRequest;
import org.dragberry.eshop.model.comment.ProductCommentResponse;
import org.dragberry.eshop.model.common.KeyValue;
import org.dragberry.eshop.model.product.ProductCategory;
import org.dragberry.eshop.model.product.ProductDetails;
import org.dragberry.eshop.model.product.ProductSearchQuery;
import org.dragberry.eshop.navigation.Breadcrumb;
import org.dragberry.eshop.service.CommentService;
import org.dragberry.eshop.service.ImageService;
import org.dragberry.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class ProductController {
	
	private static final String MODEL_CATEGORY = "category";

	private static final String MODEL_PRODUCT_LIST = "productList";

	private static final String MODEL_CATEGORY_LIST = "categoryList";
	
	private static final String MODEL_SORTING_OPTION_LIST = "sortingOptionList";

	private static final String MODEL_BREADCRUMB = "breadcrumb";

	private static final String MODEL_SEARCH_PARAMS = "searchParams"; 
	
	private static final String MSG_MENU_CATALOG = "msg.menu.catalog";

	private static final Long DEFAULT_CATEGORY_KEY = 0L;
	
	@Value("${url.catalog}")
	private String catelogReference;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	@Qualifier("templateEngine")
	private TemplateEngine templateEngine;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private ProductService productService;
	
	private LinkedHashMap<String, ProductCategory> categories;
	
	private Set<Long> categoriesInitialized = new HashSet<>();
	
	private Map<Long, Map<String, String[]>> categorySearchParams = new HashMap<>();

	private static final List<KeyValue> SORTING_OPTION_LIST = new ArrayList<>();

	static {
	    SORTING_OPTION_LIST.add(new KeyValue("msg.common.sort.popular.desc", "popular[desc]"));
	    SORTING_OPTION_LIST.add(new KeyValue("msg.common.sort.date.desc", "date[desc]"));
	    SORTING_OPTION_LIST.add(new KeyValue("msg.common.sort.price.asc", "price[desc]"));
	    SORTING_OPTION_LIST.add(new KeyValue("msg.common.sort.price.desc", "price[asc]"));
	    SORTING_OPTION_LIST.add(new KeyValue("msg.common.sort.rated.desc", "rated[desc]"));
	}
	
	/**
	 * Get the category list
	 * @return 
	 */
	public Collection<ProductCategory> getCategoryList() {
		if (categories == null) {
			fetchCategories();
		}
		return categories.values();
	}
	
	/**
     * Gen an image
     * @return
     * @throws IOException 
     */
    @GetMapping({"${url.images}/{folder}/{imageName:.+}"})
    public void getImage(HttpServletResponse response,
            @PathVariable String folder,
            @PathVariable String imageName) throws IOException {
        try (InputStream is = imageService.getImage(folder, imageName)) {
            IOUtils.copy(is, response.getOutputStream());
        }
    }
	
	/**
     * Get an product image
     * @return
	 * @throws IOException 
     */
    @GetMapping({"${url.images}/{productKey}/{productArticle}/{imageName:.+}"})
    public void getProductImage(HttpServletResponse response,
            @PathVariable Long productKey,
            @PathVariable String productArticle,
            @PathVariable String imageName) throws IOException {
        try (InputStream is = imageService.getProductImage(productKey, productArticle, imageName)) {
            IOUtils.copy(is, response.getOutputStream());
        }
    }
	
    /**
     * Get category by reference
     * @param selectedCategory
     * @return
     */
    private ProductCategory getCategory(String selectedCategory) {
    	if (categories == null) {
			fetchCategories();
		}
    	ProductCategory category = categories.getOrDefault(selectedCategory, new ProductCategory(DEFAULT_CATEGORY_KEY, "all", "Все товары"));
    	if (category == null) {
			throw new ResourceNotFoundException();
		} else {
			if (!categoriesInitialized.contains(category.getId())) {
				fetchCategoryDetails(category);
				categoriesInitialized.add(category.getId());
			}
		}
    	return category;
    }
    
    /**
	 * Fetches all category details
	 * @param category
	 */
	private void fetchCategoryDetails(ProductCategory category) {
		category.setFilters(productService.getCategoryFilters(category.getId()));
	}

	/**
	 * Fetch all available categories
	 */
	private void fetchCategories() {
		categories = productService.getCategoryList().stream().collect(Collectors.toMap(
				ProductCategory::getReference,
				ctg -> ctg,
				(k, v) -> {
					throw new IllegalStateException(MessageFormat.format("Duplicate key: {0}", k));
				},
				LinkedHashMap::new));
	}
    
    /**
     * Filter products for a category
     * @param request
     * @param selectedCategory
     * @return
     */
    @GetMapping("${url.catalog.filter}/{selectedCategory}")
    public ModelAndView search(HttpServletRequest request, @PathVariable(required = true) String selectedCategory) {
    	ModelAndView mv = new ModelAndView("pages/products/product-list :: products");
    	ProductCategory category = getCategory(selectedCategory);
		mv.addObject(MODEL_CATEGORY, category);
    	ProductSearchQuery query = new ProductSearchQuery();
    	query.setCategoryReference(selectedCategory);
    	Map<String, String[]> searchParams = new HashMap<>(request.getParameterMap());
    	categorySearchParams.put(category.getId(), searchParams);
    	query.setSearchParams(searchParams);
    	mv.addObject(MODEL_PRODUCT_LIST, productService.getProductList(query));
    	return mv;
    }
    
    /** 
     * Filter all products
     * @param request
     * @return
     */
    @GetMapping("${url.catalog.filter}")
    public ModelAndView searchAll(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("pages/products/product-list :: products");
    	ProductSearchQuery query = new ProductSearchQuery();
    	Map<String, String[]> searchParams = new HashMap<>(request.getParameterMap());
    	categorySearchParams.put(DEFAULT_CATEGORY_KEY, searchParams);
    	query.setSearchParams(searchParams);
    	mv.addObject(MODEL_PRODUCT_LIST, productService.getProductList(query));
    	return mv;
    }
    
	/**
	 * Return a list of products
	 * @return
	 */
	@GetMapping({"${url.catalog}/{selectedCategory}"})
	public ModelAndView catalog(@PathVariable String selectedCategory) {
		ModelAndView mv = new ModelAndView("pages/products/product-list");
		ProductCategory category = getCategory(selectedCategory);
		mv.addObject(MODEL_CATEGORY, category);
		mv.addObject(MODEL_BREADCRUMB, Breadcrumb.builder()
	     		.append(MSG_MENU_CATALOG, catelogReference, true)
	     		.append(category.getName(), selectedCategory));
		mv.addObject(MODEL_SORTING_OPTION_LIST, SORTING_OPTION_LIST);
		mv.addObject(MODEL_CATEGORY_LIST, getCategoryList());
		ProductSearchQuery query = new ProductSearchQuery();
		query.setCategoryReference(selectedCategory);
		Map<String, String[]> searchParams = getCategorySearchParams(category.getId());
		query.setSearchParams(searchParams);
		mv.addObject(MODEL_PRODUCT_LIST, productService.getProductList(query));
		mv.addObject(MODEL_SEARCH_PARAMS, searchParams);
		return mv;
	}
	
	/**
	 * Return a list of products
	 * @return
	 */
	@GetMapping({"${url.catalog}"})
	public ModelAndView catalogAll() {
		ModelAndView mv = new ModelAndView("pages/products/product-list");
		mv.addObject(MODEL_BREADCRUMB, Breadcrumb.builder()
				.append(MSG_MENU_CATALOG, catelogReference, true));
		mv.addObject(MODEL_SORTING_OPTION_LIST, SORTING_OPTION_LIST);
		mv.addObject(MODEL_CATEGORY_LIST, getCategoryList());
		ProductSearchQuery query = new ProductSearchQuery();
		Map<String, String[]> searchParams = getCategorySearchParams(DEFAULT_CATEGORY_KEY);
		query.setSearchParams(searchParams);
		mv.addObject(MODEL_PRODUCT_LIST, productService.getProductList(query));
		mv.addObject(MODEL_SEARCH_PARAMS, searchParams);
		return mv;
	}
	
	/**
	 * Get category search params
	 * @param categoryId
	 * @return
	 */
	private Map<String, String[]> getCategorySearchParams(Long categoryId) {
		return categorySearchParams.getOrDefault(categoryId,  Collections.emptyMap());
	}

	/**
     * Return a product page
     * @return
     */
    @GetMapping({"${url.catalog}/{categoryReference}/{productReference}"})
    public ModelAndView product(@PathVariable String categoryReference, @PathVariable String productReference) {
        if (productReference != null) {
            ProductDetails product = productService.getProductArticleDetails(categoryReference, productReference);
            if (product != null) {
            	ModelAndView mv = new ModelAndView("pages/products/details/product-details");
                mv.addObject("product", product);
                mv.addObject(MODEL_BREADCRUMB, Breadcrumb.builder()
                		.append(MSG_MENU_CATALOG, catelogReference, true)
                		.append(product.getCategory().getName(), categoryReference)
                		.append(product.getTitle(), productReference));
                return mv;
            }
        }
        throw new ResourceNotFoundException();
    }
    
    @PostMapping("${url.product.add-comment}")
    @ResponseBody
    public ResultTO<?> addComment(HttpServletRequest request, Locale locale) {
    	ProductCommentRequest cmtReq = new ProductCommentRequest();
    	String productId = request.getParameter("productId");
        try {
            cmtReq.setProductId(Long.valueOf(productId));
        } catch (NumberFormatException e) {
            throw new BadRequestException(MessageFormat.format("Invalid productId {0}", productId));
        }
    	cmtReq.setDate(LocalDateTime.now());
    	cmtReq.setIp(request.getRemoteAddr());
    	cmtReq.setName(request.getParameter("name"));
    	cmtReq.setText(request.getParameter("comment"));
    	cmtReq.setEmail(request.getParameter("email"));
    	String mark = request.getParameter("productRating");
    	try {
    	    cmtReq.setMark(Integer.valueOf(mark));
    	} catch (NumberFormatException e) {
			throw new BadRequestException(MessageFormat.format("Invalid mark {0}", mark));
		}
    	ResultTO<ProductCommentResponse> resp = commentService.createComment(cmtReq);
    	if (resp.hasIssues()) {
    	    resp.getIssues().forEach(issue -> issue.setMessage(messageSource.getMessage(issue.getErrorCode(), issue.getParams().toArray(), locale)));
    		return resp;
    	} else {
    		Context context = new Context();
    		context.setVariable("comment", resp.getValue());
            return Results.create(templateEngine.process("pages/products/details/product-details-tab-panel",
    				new HashSet<>(Arrays.asList("product-comment")), context));
    	}
    }
}