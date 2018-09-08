package org.dragberry.eshop.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dragberry.eshop.dal.entity.Category;
import org.dragberry.eshop.dal.entity.Image;
import org.dragberry.eshop.dal.entity.Product;
import org.dragberry.eshop.dal.entity.ProductArticle;
import org.dragberry.eshop.dal.entity.ProductArticleOption;
import org.dragberry.eshop.dal.entity.Product.SaleStatus;
import org.dragberry.eshop.dal.repo.CategoryRepository;
import org.dragberry.eshop.dal.repo.ProductArticleOptionRepository;
import org.dragberry.eshop.dal.repo.ProductArticleRepository;
import org.dragberry.eshop.dal.repo.ProductRepository;
import org.dragberry.eshop.service.DataImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j;

@Log4j
@Service("InSalesDataImporter")
public class InSalesDataImporter implements DataImporter {

	private static final String PRODUCT_ARTICLE = "Артикул";
	
	private static final String PRODUCT_DESCRIPTION = "Краткое описание";
	
	private static final String PRODUCT_DESCRIPTION_FULL = "Полное описание";
	
	private static final String PRODUCT_TITLE = "Название товара";

	private static final String PRODUCT_REFERENCE = "Название товара в URL";

	private static final String TAG_TITLE = "Тег title";
	
	private static final String TAG_KEYWORDS = "Мета-тег keywords";
	
	private static final String TAG_DESCRIPTION = "Мета-тег description";

	private static final String ACTUAL_PRICE = "Цена продажи";
	
	private static final String PRICE = "Старая цена";
	
	private static final String IMAGES = "Изображения";
	
	private static final String CATEGORIES = "Размещение на сайте";

	private static final String QUANTITY = "Остаток";
	
	private static final String OPTION = "Свойство:";
	
	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private ProductArticleRepository productArticleRepo;
	
	@Autowired
	private ProductArticleOptionRepository paoRepo;
	
	private Map<String, Integer> columnsMap = new HashMap<>();
	
	private Map<String, String> optionsMap = new HashMap<>();
	
	private Map<String, Category> categoryMap = new HashMap<>();
	
	@PostConstruct
	public void init() {
		createCategory("Каталог смарт-часов", "Смарт-часы", 0);
		createCategory("Каталог фитнес-браслетов", "Фитнес-браслеты", 2);
		createCategory("Прочие аксессуары", "Прочие аксессуары", 3);
		createCategory("Каталог детских смарт-часов", "Детские смарт-часы", 1);
	}
	
	/**
     * Process category
     * @param categoryName
     * @return category
     */
	private void createCategory(String externalName, String name, int order) {
		Category ctg = null;
		for (int index = 0; ctg == null && index < 3; index++) {
			try {
				ctg = categoryMap.put(externalName, categoryRepo.findByName(name).orElseGet(() -> {
					Category newCtg = new Category();
					newCtg.setName(name);
					newCtg.setReference(name);
					newCtg.setOrder(order);
					return categoryRepo.save(newCtg);
				}));
			} catch (Exception exc) {
				log.warn(MessageFormat.format("It looks like a category with the same name {0} already exists", name), exc);
			}
		}
	}
	
	@Override
	public void importData(InputStream is) {
		try {
			List<String[]> rawArticle = new ArrayList<>();
			List<String> lines = IOUtils.readLines(is, StandardCharsets.UTF_16);
			for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
				String line = lines.get(lineIndex);
				if (StringUtils.isNotBlank(line)) {
					if (line.startsWith("ID")) {
						processFirstLine(line);
					} else {
						String[] columns = line.split("\t");
						String currentLineArticle = columns[columnsMap.get(PRODUCT_ARTICLE)];
						if (StringUtils.isBlank(currentLineArticle)) {
							throw new NullPointerException("Current line article is null!");
						}
						if (!rawArticle.isEmpty() && !rawArticle.get(0)[columnsMap.get(PRODUCT_ARTICLE)].equals(currentLineArticle)) {
							processRawArticle(rawArticle.get(0)[columnsMap.get(PRODUCT_ARTICLE)], rawArticle);
							rawArticle = new ArrayList<>();
						}
						rawArticle.add(columns);
						if (lineIndex == lines.size() - 1) {
							processRawArticle(rawArticle.get(0)[columnsMap.get(PRODUCT_ARTICLE)], rawArticle);
						}
						
						log.info(line);
					}
				}
			}
		} catch (IOException ioe) {
			log.error("An error has occurred durin importing file!", ioe);
		}

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void processRawArticle(String article, List<String[]> rawArticle) {
		if (!rawArticle.isEmpty()) {
			ProductArticle pa = productArticleRepo.findByArticle(article).orElse(new ProductArticle());
			String[] firstLine = rawArticle.get(0);
			pa.setArticle(article);
			pa.setTitle(firstLine[columnsMap.get(PRODUCT_TITLE)]);
			pa.setReference(firstLine[columnsMap.get(PRODUCT_REFERENCE)]);
			pa.setDescription(firstLine[columnsMap.get(PRODUCT_DESCRIPTION)]);
			pa.setDescriptionFull(firstLine[columnsMap.get(PRODUCT_DESCRIPTION_FULL)]);
			pa.setTagTitle(firstLine[columnsMap.get(TAG_TITLE)]);
			pa.setTagKeywords(firstLine[columnsMap.get(TAG_KEYWORDS)]);
			pa.setTagDescription(firstLine[columnsMap.get(TAG_DESCRIPTION)]);
			
//			processImages(firstLine, pa);
			
			for (String[] line : rawArticle) {
				pa.setCategories(new ArrayList<>());
				String categoryLine = getProperty(line, CATEGORIES);
				for (Map.Entry<String, Category> entry : categoryMap.entrySet()) {
					if (categoryLine.contains(entry.getKey())) {
						pa.getCategories().add(entry.getValue());
					}
				}
			}
			
			pa = productArticleRepo.save(pa);
			
			for (String[] line: rawArticle) {
				Set<ProductArticleOption> options = new HashSet<>();
				for (Entry<String, String> entry : optionsMap.entrySet()) {
					String optionValue = line[columnsMap.get(entry.getKey())];
					if (StringUtils.isNotBlank(optionValue)) {
						ProductArticleOption pao = paoRepo.findByProductArticleAndNameAndValue(pa, entry.getValue(), optionValue);
						if (pao == null) {
							pao = new ProductArticleOption();
							pao.setName(entry.getValue());
							pao.setValue(optionValue);
							pao.setProductArticle(pa);
							pao = paoRepo.save(pao);
						}
						options.add(pao);
					}
				}
				
				Product product = new Product();
				product.setSaleStatus(SaleStatus.EXPOSED);
				product.setProductArticle(pa);
				product.setOptions(options);
				try {
					product.setPrice(new BigDecimal(getProperty(line, PRICE)));
				} catch(NumberFormatException | NullPointerException nfe) {
					log.warn(MessageFormat.format("Can't parse price {0} for article {1}", getProperty(line, PRICE), pa.getArticle()));
				}
				try {
					product.setActualPrice(new BigDecimal(getProperty(line, ACTUAL_PRICE)));
					if (product.getPrice() == null) {
						product.setPrice(product.getActualPrice());
					}
				} catch(NumberFormatException | NullPointerException nfe) {
					log.warn(MessageFormat.format("Can't parse actual price {0} for article {1}", getProperty(line, ACTUAL_PRICE), pa.getArticle()));
				}
				try {
					product.setQuantity(Integer.valueOf(getProperty(line, QUANTITY)));
				} catch(NumberFormatException | NullPointerException nfe) {
					log.warn(MessageFormat.format("Can't parse quantity {0} for article {1}", getProperty(line, QUANTITY), pa.getArticle()));
				}
				productRepo.save(product);
			}
		}
		
	}

	private void processImages(String[] columns, ProductArticle pa) {
		String[] imgs = columns[columnsMap.get(IMAGES)].split(" ");
		for (int imgIndex = 0; imgIndex < imgs.length; imgIndex++) {
			String imgURL = imgs[imgIndex];
			log.info(MessageFormat.format("Image: {0}", imgURL));
			int lastIndexOfSlash = imgURL.lastIndexOf("/");
			String realURL = imgURL.substring(0, lastIndexOfSlash + 1) + URLEncoder.encode(imgURL.substring(lastIndexOfSlash + 1), StandardCharsets.UTF_8);
			try {
				URLConnection conn = new URL(realURL).openConnection();
				InputStream imgIS = conn.getInputStream();
				if (imgIndex == 0) {
					Image mainImage = new Image();
					mainImage.setContent(IOUtils.toByteArray(imgIS));
					mainImage.setName(pa.getArticle());
					mainImage.setType("image/" + imgURL.substring(imgURL.lastIndexOf(".")));
					pa.setMainImage(mainImage);
				}
				IOUtils.close(conn);
			} catch (MalformedURLException exc) {
				log.error("An error has occurred during loading image! Invalid URL!", exc);
			} catch (IOException exc) {
				log.error("An error has occurred during loading image! IO error!", exc);
			}
		}
	}

	private void processFirstLine(String line) {
		String[] columns = line.split("\t");
		for (int i = 0; i < columns.length; i++ ) {
			log.info(MessageFormat.format("Column [{0}]: [{1}]", i, columns[i]));
			String str = columns[i];
			columnsMap.put(str, i);
			if (str.startsWith(OPTION)) {
				String optionName = StringUtils.trim(str.substring(str.lastIndexOf(":") + 1));
				optionsMap.put(str, optionName);
			}
		}
		
	}
	
	private String getProperty(String[] columns, String columnName) {
		Integer index = columnsMap.get(columnName);
		return index < columns.length ? columns[index] : null;
	}

}
