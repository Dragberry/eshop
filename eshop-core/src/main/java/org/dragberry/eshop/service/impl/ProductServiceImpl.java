package org.dragberry.eshop.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.dragberry.eshop.dal.entity.Product;
import org.dragberry.eshop.dal.entity.ProductArticle;
import org.dragberry.eshop.dal.repo.CategoryRepository;
import org.dragberry.eshop.dal.repo.ImageRepository;
import org.dragberry.eshop.dal.repo.ProductArticleRepository;
import org.dragberry.eshop.dal.repo.ProductRepository;
import org.dragberry.eshop.model.cart.CapturedProduct;
import org.dragberry.eshop.model.common.ImageModel;
import org.dragberry.eshop.model.common.KeyValue;
import org.dragberry.eshop.model.common.Modifier;
import org.dragberry.eshop.model.product.ProductListItem;
import org.dragberry.eshop.model.product.ActualPriceHolder;
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
    private ImageRepository imageRepo;
    
    @Autowired
    private ProductArticleRepository productArticleRepo;
    
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
	    List<ProductArticle> searchResult;
	    if (StringUtils.isBlank(query.getCategoryReference())) {
	        searchResult = productArticleRepo.findAll();
	    } else {
	        searchResult = productArticleRepo.findByCategoryReference(query.getCategoryReference());
	    }
		return searchResult.stream().map(article -> {
			ProductListItem product = new ProductListItem();
			product.setId(article.getEntityKey());
			product.setTitle(article.getTitle());
			product.setArticle(article.getArticle());
			product.setReference(article.getReference());
			product.setMainImage(article.getMainImage() != null ? article.getMainImage().getEntityKey() : null);
			setLowestPrice(article, product);
			
			// Test data
			product.setCommentsCount(3);
			product.setLabels(Map.of("Скидка", Modifier.INFO, "20%", Modifier.DANGER));
			product.setRating(3.3);
			return product;
		}).collect(Collectors.toList());
	}

    private void setLowestPrice(ProductArticle article, ActualPriceHolder product) {
        Product lowerPriceProduct = null;
        BigDecimal lowerPrice = null;
        BigDecimal lowerActualPrice = null;
        for (var p : article.getProducts()) {
            if (lowerPriceProduct == null && p.getPrice() != null) {
                lowerPriceProduct = p;
                lowerPrice = p.getPrice();
                lowerActualPrice = p.getActualPrice();
            } else {
                if (lowerPrice.compareTo(p.getPrice()) == -1) {
                    lowerPrice = p.getPrice();
                    
                }
                if (lowerActualPrice != null) {
                    if (p.getActualPrice() != null && lowerActualPrice.compareTo(p.getActualPrice()) == -1) {
                        lowerPrice = p.getActualPrice();
                    }
                } else {
                    lowerActualPrice = p.getActualPrice();
                }
            }
        }
        product.setPrice(lowerPrice);
        product.setActualPrice(lowerActualPrice);
    }
	
    @Override
    public ProductDetails getProductArticleDetails(String productReference) {
        var article = productArticleRepo.findByReference(productReference);
        if (article == null) {
            return null;
        }
        ProductDetails product = new ProductDetails();
        product.setId(article.getEntityKey());
        product.setArticle(article.getArticle());
        product.setTitle(article.getTitle());
        product.setDescription(article.getDescription());
        product.setMainImage(article.getMainImage() != null ? article.getMainImage().getEntityKey() : null);
       
        Map<String, Set<KeyValue>> optionValues = new HashMap<>();
        Map<Long, Set<KeyValue>> productOptions = new HashMap<>();
        
        article.getProducts().forEach(p -> {
            p.getOptions().forEach(o -> {
                optionValues.computeIfAbsent(
                        o.getName(),
                        value -> new HashSet<KeyValue>()).add(new KeyValue(o.getEntityKey(), o.getValue()));
            });
            productOptions.put(
            		p.getEntityKey(),
                    p.getOptions().stream().map(o -> new KeyValue(o.getName(), o.getEntityKey())).collect(Collectors.toSet()));
        });
        product.setOptionValues(optionValues);
        product.setProductOptions(productOptions);
        setLowestPrice(article, product);
        // test data
        product.setLabels(Map.of("Скидка", Modifier.INFO, "20%", Modifier.DANGER));
        return product;
    }
    
    @Override
    public ImageModel getProductImage(Long productKey, Long imageKey) {
        var image = imageRepo.findById(imageKey).get();
        ImageModel img = new ImageModel();
        img.setContent(image.getContent());
        img.setId(image.getEntityKey());
        img.setName(image.getName());
        img.setType(image.getType());
        return img;
    }
    
    @Override
    public CapturedProduct getProductCartDetails(CapturedProduct capturedProduct) {
        Product product = productRepo.findById(capturedProduct.getProductId()).get();
        capturedProduct.setArticle(product.getProductArticle().getArticle());
        capturedProduct.setTitle(product.getProductArticle().getTitle());
        capturedProduct.setReference(product.getProductArticle().getReference());
        capturedProduct.setPrice(product.getActualPrice() != null ? product.getActualPrice() : product.getPrice());
        capturedProduct.setOptions(product.getOptions().stream().map(o -> new KeyValue(o.getName(), o.getValue())).collect(Collectors.toSet()));
        capturedProduct.setMainImage(productArticleRepo.findMainImageKey(capturedProduct.getProductArticleId()));
        capturedProduct.updateFullTitle();
        return capturedProduct;
    }
}
