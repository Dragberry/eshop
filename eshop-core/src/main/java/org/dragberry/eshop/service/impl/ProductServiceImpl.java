package org.dragberry.eshop.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.dragberry.eshop.dal.entity.Category;
import org.dragberry.eshop.dal.entity.Product;
import org.dragberry.eshop.dal.entity.Product.SaleStatus;
import org.dragberry.eshop.dal.entity.ProductArticle;
import org.dragberry.eshop.dal.entity.ProductArticleOption;
import org.dragberry.eshop.dal.repo.CategoryRepository;
import org.dragberry.eshop.dal.repo.ProductArticleOptionRepository;
import org.dragberry.eshop.dal.repo.ProductArticleRepository;
import org.dragberry.eshop.dal.repo.ProductRepository;
import org.dragberry.eshop.model.common.Modifier;
import org.dragberry.eshop.model.product.ProductListItem;
import org.dragberry.eshop.model.product.ProductCategory;
import org.dragberry.eshop.model.product.ProductDetails;
import org.dragberry.eshop.model.product.ProductSearchQuery;
import org.dragberry.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private CategoryRepository categoryRepo;
    
    @Autowired
    private ProductArticleRepository productArticleRepo;
    
    @Autowired
    private ProductArticleOptionRepository productArticleOptionRepo;
    
    @Autowired
    private ProductRepository productRepo;
    
	public List<ProductCategory> getCategoryList() {
		return StreamSupport.stream(categoryRepo.findAll().spliterator(), false).map(category -> {
			ProductCategory pc = new ProductCategory();
			pc.setName(category.getName());
			pc.setReference(category.getReference());
			return pc;
		}).collect(Collectors.toList());
	}

	@Override
	public List<ProductListItem> getProductList(ProductSearchQuery query) {
		if (StringUtils.isNotBlank(query.getCategoryReference())) {
			return productArticleRepo.findAll().stream().map(article -> {
				ProductListItem product = new ProductListItem();
				product.setId(article.getEntityKey());
				product.setTitle(article.getTitle());
				product.setReference(article.getReference());
				product.setPrice(article.getProducts().stream().min((p1, p2) -> p1.getPrice().compareTo(p2.getPrice())).get().getPrice());
				// Test data
				product.setCommentsCount(3);
				product.setLabels(Map.of("Скидка", Modifier.INFO, "20%", Modifier.DANGER));
				product.setRating(3.3);
				return product;
			}).collect(Collectors.toList());
		}
		return List.of();
	}
	
    @Override
    public ProductDetails getProduct(String productReference) {
        ProductDetails product = new ProductDetails();
        product.setTitle("Смарт-часы DZ09");
        product.setArticle("DZ09");
        product.setActualPrice(new BigDecimal(100));
        product.setPrice(new BigDecimal(200));
        product.setDescription("Интернет-магазин Smartvitrina.by предлагает Вам стильные и многофункциональные смарт-часы DZ09 (Smart watch DZ09) по выгодной цене.");
        product.setLabels(Map.of("Скидка", Modifier.INFO, "20%", Modifier.DANGER));
        return product;
    }
}
