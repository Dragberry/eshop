package org.dragberry.eshop.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.dragberry.eshop.model.bootstrap.Modifier;
import org.dragberry.eshop.model.product.Product;
import org.dragberry.eshop.model.product.ProductCategory;
import org.dragberry.eshop.model.product.ProductSearchQuery;
import org.dragberry.eshop.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

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
}
