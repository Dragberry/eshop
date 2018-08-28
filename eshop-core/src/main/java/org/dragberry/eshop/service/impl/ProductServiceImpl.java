package org.dragberry.eshop.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dragberry.eshop.dal.entity.Category;
import org.dragberry.eshop.dal.entity.Product.SaleStatus;
import org.dragberry.eshop.dal.entity.ProductArticle;
import org.dragberry.eshop.dal.entity.ProductArticleOption;
import org.dragberry.eshop.dal.repo.CategoryRepository;
import org.dragberry.eshop.dal.repo.ProductArticleOptionRepository;
import org.dragberry.eshop.dal.repo.ProductArticleRepository;
import org.dragberry.eshop.dal.repo.ProductRepository;
import org.dragberry.eshop.model.common.Modifier;
import org.dragberry.eshop.model.product.Product;
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
		return List.of(
				new ProductCategory(1L, "smart-watches", "Смарт-часы"),
				new ProductCategory(2L, "children-smart-watches", "Детские смарт-часы"),
				new ProductCategory(3L, "fitness-trackers", "Фитнес-браслеты"),
				new ProductCategory(4L, "accessories", "Прочие аксессуары"));
	}

	@Override
	public List<Product> getProductList(ProductSearchQuery query) {
		return List.of(
				new Product(1L,  "U8", "Смарт часы U8", "smart-watch-u8", new BigDecimal(100), new BigDecimal(200), 3, 4.3, Map.of("Скидка", Modifier.INFO, "20%", Modifier.DANGER), null),
				new Product(2L,  "GT08", "Смарт часы GT08", "smart-watch-gt08", new BigDecimal(100), new BigDecimal(200), 5, 3.4, Map.of("Скидка", Modifier.INFO, "20%", Modifier.DANGER), null),
				new Product(3L,  "DZ09", "Смарт часы DZ09", "smart-watch-dz09", new BigDecimal(100), new BigDecimal(200), 123, 4.8, Map.of("Скидка", Modifier.INFO, "25%", Modifier.DANGER), null),
				new Product(4L,  "A1", "Смарт часы A1", "smart-watch-a1", new BigDecimal(100), new BigDecimal(200), 1023, 1.2, Map.of("Скидка", Modifier.INFO, "10%", Modifier.DANGER), null),
				new Product(5L,  "GV18", "Смарт часы GV18", "smart-watch-gv18", new BigDecimal(100), new BigDecimal(200), 0, 2.7, Map.of("Скидка", Modifier.INFO, "20%", Modifier.DANGER), null));
	}

	private void test() {
	    var c = new Category();
	    c.setName("Cмарт-часы");
	    c.setReference("smart-watch");
	    c = categoryRepo.save(c);
	    
	    var pa = new ProductArticle();
	    pa.setArticle("DZ09");
	    pa.setTitle("Смарт-часы DZ09");
	    pa.setReference("smart-watch-09");
	    pa.setCategories(List.of(c));
	    pa = productArticleRepo.save(pa);
	    
	    var pao1 = new ProductArticleOption();
	    pao1.setName("Цвет");
	    pao1.setValue("Красный");
	    pao1.setProductArticle(pa);
	    pao1 = productArticleOptionRepo.save(pao1);
	    var pao2 = new ProductArticleOption();
	    pao2.setName("Цвет");
	    pao2.setValue("Зеленый");
	    pao2.setProductArticle(pa);
	    pao2 = productArticleOptionRepo.save(pao2);
	    var pao3 = new ProductArticleOption();
	    pao3.setName("Размер");
	    pao3.setValue("Маленький");
	    pao3.setProductArticle(pa);
	    pao3 = productArticleOptionRepo.save(pao3);
	    
	    var p1 = new org.dragberry.eshop.dal.entity.Product();
	    p1.setProductArticle(pa);
	    p1.setOptions(Set.of(pao1, pao3));
	    p1.setPrice(new BigDecimal(100));
	    p1.setQuantity(4);
	    p1.setSaleStatus(SaleStatus.EXPOSED);
	    p1.setActualPrice(new BigDecimal(75));
	    p1 = productRepo.save(p1);
	    
	    var p2 = new org.dragberry.eshop.dal.entity.Product();
	    p2.setProductArticle(pa);
	    p2.setOptions(Set.of(pao2, pao3));
	    p2.setPrice(new BigDecimal(50));
	    p2.setQuantity(0);
	    p2.setSaleStatus(SaleStatus.OUT_OF_STOCK);
	    p2.setActualPrice(new BigDecimal(25));
	    p2 = productRepo.save(p2);
	    
	}
	
    @Override
    public ProductDetails getProduct(String productReference) {
        test();
        ProductDetails product = new ProductDetails();
        product.setTitle("Смарт-часы DZ09");
        product.setArticle("DZ09");
        product.setPrice(new BigDecimal(100));
        product.setOldPrice(new BigDecimal(200));
        product.setDescription("Интернет-магазин Smartvitrina.by предлагает Вам стильные и многофункциональные смарт-часы DZ09 (Smart watch DZ09) по выгодной цене.");
        product.setLabels(Map.of("Скидка", Modifier.INFO, "20%", Modifier.DANGER));
        return product;
    }
}
