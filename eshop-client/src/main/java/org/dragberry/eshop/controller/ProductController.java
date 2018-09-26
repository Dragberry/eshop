package org.dragberry.eshop.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dragberry.eshop.common.ResultTO;
import org.dragberry.eshop.common.Results;
import org.dragberry.eshop.controller.exception.BadRequestException;
import org.dragberry.eshop.controller.exception.ResourceNotFoundException;
import org.dragberry.eshop.model.comment.ProductCommentRequest;
import org.dragberry.eshop.model.comment.ProductCommentResponse;
import org.dragberry.eshop.model.common.ImageModel;
import org.dragberry.eshop.model.common.KeyValue;
import org.dragberry.eshop.model.product.ProductCategory;
import org.dragberry.eshop.model.product.ProductDetails;
import org.dragberry.eshop.model.product.ProductSearchQuery;
import org.dragberry.eshop.navigation.Breadcrumb;
import org.dragberry.eshop.service.CommentService;
import org.dragberry.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.ibm.icu.text.MessageFormat;

@Controller
public class ProductController {
	
	private static final String MODEL_CATEGORY = "category";

	private static final String MODEL_PRODUCT_LIST = "productList";

	private static final String MODEL_CATEGORY_LIST = "categoryList";
	
	private static final String MODEL_SORTIN_OPTION_LIST = "sortingOptionList";

	private static final String MODEL_BREADCRUMB = "breadcrumb";

	private static final String MSG_MENU_CATALOG = "msg.menu.catalog";

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
	private ProductService productService;
	
	private static final List<KeyValue> SORTIN_OPTION_LIST = new ArrayList<>(); {
	    SORTIN_OPTION_LIST.add(new KeyValue("msg.common.sort.popular.desc", "popular[desc]"));
	    SORTIN_OPTION_LIST.add(new KeyValue("msg.common.sort.date.desc", "date[desc]"));
	    SORTIN_OPTION_LIST.add(new KeyValue("msg.common.sort.price.asc", "price[desc]"));
	    SORTIN_OPTION_LIST.add(new KeyValue("msg.common.sort.price.desc", "price[asc]"));
	    SORTIN_OPTION_LIST.add(new KeyValue("msg.common.sort.rated.desc", "rated[desc]"));
	}
	
	/**
     * Serves images
     * @return
	 * @throws IOException 
     */
    @GetMapping({"${url.products-images}/{productKey}/{imageKey}/{imageName}"})
    public void productMainImage(HttpServletResponse response,
            @PathVariable Long productKey,
            @PathVariable Long imageKey,
            @PathVariable String imageName) throws IOException {
        ImageModel img = productService.getProductImage(productKey, imageKey);
        if (img != null) {
            response.setContentType(img.getType());
            IOUtils.copy(new ByteArrayInputStream(img.getContent()), response.getOutputStream());
        }
    }
	
    @GetMapping("${url.catalog.filter}/{selectedCategory}")
    public ModelAndView search(HttpServletRequest request, @PathVariable String selectedCategory) {
    	ProductCategory category = productService.findCategory(selectedCategory);
    	if (category == null) {
    	    throw new ResourceNotFoundException();
    	}
        ModelAndView mv = new ModelAndView("pages/products/product-list :: products");
    	ProductSearchQuery query = new ProductSearchQuery();
    	query.setCategoryReference(selectedCategory);
    	query.setSearchParams(request.getParameterMap());
    	mv.addObject(MODEL_CATEGORY, category);
        mv.addObject(MODEL_PRODUCT_LIST, productService.getProductList(query));
    	return mv;
    }
    
    @GetMapping("${url.catalog.filter}")
    public ModelAndView searchAll(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("pages/products/product-list :: products");
    	ProductSearchQuery query = new ProductSearchQuery();
    	query.setSearchParams(request.getParameterMap());
        mv.addObject(MODEL_PRODUCT_LIST, productService.getProductList(query));
    	return mv;
    }
    
    
	/**
	 * Return a list of products
	 * @return
	 */
	@GetMapping({"${url.catalog}", "${url.catalog}/{selectedCategory}"})
	public ModelAndView catalog(@PathVariable(required = false) String selectedCategory) {
		ModelAndView mv = new ModelAndView("pages/products/product-list");
		List<ProductCategory> categoryList = productService.getCategoryList();
		ProductSearchQuery query = new ProductSearchQuery();
		query.setCategoryReference(selectedCategory);
		if (StringUtils.isNotBlank(selectedCategory)) {
			ProductCategory category = categoryList.stream().filter(c -> c.getReference().equals(selectedCategory)).findFirst().orElse(new ProductCategory(0L, "all", "Все товары"));
			if (category == null) {
				throw new ResourceNotFoundException();
			} else {
				category.setFilters(productService.getCategoryFilters(category.getId()));
			}
			mv.addObject(MODEL_CATEGORY, category);
			mv.addObject(MODEL_BREADCRUMB, Breadcrumb.builder()
		     		.append(MSG_MENU_CATALOG, catelogReference, true)
		     		.append(category.getName(), selectedCategory));
		} else {
			mv.addObject(MODEL_BREADCRUMB, Breadcrumb.builder()
		     		.append(MSG_MENU_CATALOG, catelogReference, true));
		}
		mv.addObject(MODEL_SORTIN_OPTION_LIST, SORTIN_OPTION_LIST);
		mv.addObject(MODEL_CATEGORY_LIST, categoryList);
		mv.addObject(MODEL_PRODUCT_LIST, productService.getProductList(query));
		return mv;
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