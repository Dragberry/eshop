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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dragberry.eshop.dal.entity.Category;
import org.dragberry.eshop.dal.entity.CategoryFilter;
import org.dragberry.eshop.dal.entity.CategoryFilterAllList;
import org.dragberry.eshop.dal.entity.CategoryFilterAnyString;
import org.dragberry.eshop.dal.entity.Image;
import org.dragberry.eshop.dal.entity.Product;
import org.dragberry.eshop.dal.entity.ProductArticle;
import org.dragberry.eshop.dal.entity.ProductArticleOption;
import org.dragberry.eshop.dal.entity.ProductAttribute;
import org.dragberry.eshop.dal.entity.ProductAttributeBoolean;
import org.dragberry.eshop.dal.entity.ProductAttributeList;
import org.dragberry.eshop.dal.entity.ProductAttributeNumeric;
import org.dragberry.eshop.dal.entity.ProductAttributeString;
import org.dragberry.eshop.dal.entity.Product.SaleStatus;
import org.dragberry.eshop.dal.repo.CategoryRepository;
import org.dragberry.eshop.dal.repo.ImageRepository;
import org.dragberry.eshop.dal.repo.ProductArticleOptionRepository;
import org.dragberry.eshop.dal.repo.ProductArticleRepository;
import org.dragberry.eshop.dal.repo.ProductRepository;
import org.dragberry.eshop.service.DataImporter;
import org.dragberry.eshop.service.TransliteService;
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
	
	private static final String ATTRIBUTE = "Параметр:";
	
	private static final Pattern PATTERN_EXISTS = Pattern.compile("есть \\((.*?)\\)$");

	private static final Pattern PATTERN_EXISTS_VALUES = Pattern.compile("есть \\((.*?) (.*?)\\)$");
	
	private static final Pattern PATTERN_SCREEN = Pattern.compile("(цветной)?\\s*(сенсорный)?\\s*(TFT|AMOLED|IPS|ЖК)?[- ]?дисплей?\\s*(.*)");
	
	private static final String GRP_ATTR_GENERAL = "Основные";
	
	private static final String GRP_ATTR_INTERFACES = "Интерфейсы";
	
	private static final String GRP_ATTR_FUNCTIONS = "Функции";
	
	private static final String GRP_ATTR_CONSTRUCTION = "Конструкция";
	
	private static final String GRP_ATTR_SCREEN = "Экран";
	
	private static final String GRP_ATTR_ACCUM = "Аккумулятор и время работы";
	
	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private ImageRepository imageRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private ProductArticleRepository productArticleRepo;
	
	@Autowired
	private ProductArticleOptionRepository paoRepo;
	
	@Autowired
	private TransliteService transliteService;
	
	private Map<String, Integer> columnsMap = new HashMap<>();
	
	private Map<String, String> optionsMap = new HashMap<>();
	
	private Map<String, String> attributeMap = new HashMap<>();
	
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
					newCtg.setReference(transliteService.transformToId(name));
					newCtg.setOrder(order);
					List<CategoryFilter<?,?,?>> filters = new ArrayList<>();
					CategoryFilterAnyString tech = new CategoryFilterAnyString();
					tech.setCategory(newCtg);
					tech.setName("Технология дисплея");
					tech.setOrder(0);
					filters.add(tech);
					CategoryFilterAnyString dim = new CategoryFilterAnyString();
					dim.setCategory(newCtg);
					dim.setName("Разрешение дисплея");
					dim.setOrder(1);
					filters.add(dim);
					newCtg.setFilters(filters);
					CategoryFilterAllList ff = new CategoryFilterAllList();
					ff.setCategory(newCtg);
					ff.setName("Фитнес-функции");
					ff.setOrder(2);
					filters.add(ff);
					newCtg.setFilters(filters);
					CategoryFilterAllList aps = new CategoryFilterAllList();
					aps.setCategory(newCtg);
					aps.setName("Встроенные приложения");
					aps.setOrder(3);
					filters.add(aps);
					newCtg.setFilters(filters);
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
			pa.setReference(transliteService.transformToId(firstLine[columnsMap.get(PRODUCT_REFERENCE)]));
			pa.setDescription(firstLine[columnsMap.get(PRODUCT_DESCRIPTION)]);
			pa.setDescriptionFull(firstLine[columnsMap.get(PRODUCT_DESCRIPTION_FULL)]);
			pa.setTagTitle(firstLine[columnsMap.get(TAG_TITLE)]);
			pa.setTagKeywords(firstLine[columnsMap.get(TAG_KEYWORDS)]);
			pa.setTagDescription(firstLine[columnsMap.get(TAG_DESCRIPTION)]);
			
			List<ProductAttribute<?>> attributes = processAttributes(pa, firstLine);
			pa.getAttributes().clear();
			if (!attributes.isEmpty()) {
				productArticleRepo.flush();
				pa.getAttributes().addAll(attributes);
			}
			
			List<Image> oldImages = new ArrayList<>(pa.getImages());
			processImages(firstLine, pa);
			
			for (String[] line : rawArticle) {
				pa.setCategories(new ArrayList<>());
				String categoryLine = getProperty(line, CATEGORIES);
				for (Map.Entry<String, Category> entry : categoryMap.entrySet()) {
					if (categoryLine.contains(entry.getKey())) {
						pa.getCategories().add(entry.getValue());
					}
				}
			}
			
			productRepo.deleteAll(pa.getProducts());
			pa.getProducts().clear();
            pa = productArticleRepo.save(pa);
            
			imageRepo.deleteAll(oldImages);
			
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
	
	private static void processListAttribute(String group, String name, String attrValue, ProductArticle pa, List<ProductAttribute<?>> attributes, Integer order) {
		String[] values = attrValue.split("##");
		if (values.length > 0) {
			List<String> valueList = new ArrayList<>();
			valueList.addAll(List.of(values));
			attributes.add(ProductAttributeList.of(pa, group, name, valueList, order));
		}
	}
	
	private static void processListOfBoolAttribute(String group, String name, String attrValue, ProductArticle pa, List<ProductAttribute<?>> attributes, Integer order) {
		Stream.of(attrValue.split("##")).forEach(attr -> {
			attributes.add(ProductAttributeBoolean.of(pa, group, attr, Boolean.TRUE, order));
		});
	}

	private List<ProductAttribute<?>> processAttributes(ProductArticle pa, String[] firstLine) {
		List<ProductAttribute<?>> attributes = new ArrayList<>();
		for (Entry<String, String> entry : attributeMap.entrySet()) {
		    Integer columnNumber = columnsMap.get(entry.getKey());
		    String attrValue = firstLine.length > columnNumber ? firstLine[columnNumber] : StringUtils.EMPTY;
		    if (StringUtils.isNotBlank(attrValue)) {
		        switch (entry.getValue()) {
		        	case "Тип аккумулятора":
		        		String[] acc = attrValue.split("[^A-Za-z0-9]+");
		        		attributes.add(ProductAttributeNumeric.of(
		        				pa, GRP_ATTR_ACCUM, entry.getValue(), new BigDecimal(acc[0]), acc[1], 300));
		        		break;
		        	case "Оперативная память":
		        		String[] ram = attrValue.split("\\s+");
		        		attributes.add(ProductAttributeNumeric.of(
		        				pa, GRP_ATTR_GENERAL, entry.getValue(), new BigDecimal(ram[0]), ram[1], 1));
		        		break;
		        	case "Встроенная память":
		        		String[] rom = attrValue.split("\\s+");
		        		attributes.add(ProductAttributeNumeric.of(
		        				pa, GRP_ATTR_GENERAL, entry.getValue(), new BigDecimal(rom[0]), rom[1], 2));
		        		break;
		        	case "Совместимость":
		        		processListAttribute(GRP_ATTR_GENERAL, entry.getValue(), attrValue, pa, attributes, 0);
		        		break;
		        	case "Связь":
		        		processListOfBoolAttribute(GRP_ATTR_INTERFACES, entry.getValue(), attrValue, pa, attributes, 100);
		        		break;
		        	case "Поддержка SIM-карты":
	        			String desc = null;
	        			Matcher simMatcher = PATTERN_EXISTS.matcher(attrValue);
		        		if (simMatcher.find()) {
		        			desc = simMatcher.group(1);
		        		}
		        		attributes.add(ProductAttributeBoolean.of(pa, GRP_ATTR_CONSTRUCTION, entry.getValue(), desc != null, desc, 200));
		        		break;
		        	case "Материал корпуса":
		        		attributes.add(ProductAttributeString.of(pa, GRP_ATTR_CONSTRUCTION, entry.getValue(), attrValue, 204));
		        		break;
		        	case "Материал ремешка":
		        		attributes.add(ProductAttributeString.of(pa, GRP_ATTR_CONSTRUCTION, entry.getValue(), attrValue, 205));
		        		break;
		        	case "Вес":
		        		String[] weight = attrValue.split("\\s+");
		        		attributes.add(ProductAttributeNumeric.of(
		        				pa, GRP_ATTR_CONSTRUCTION, entry.getValue(), new BigDecimal(weight[0]), weight[1], 201));
		        		break;
		        	case "Встроенные приложения":
		        		processListAttribute(GRP_ATTR_FUNCTIONS, entry.getValue(), attrValue, pa, attributes, 501);
		    			break;
		        	case "Фитнес-функции":
		        		processListAttribute(GRP_ATTR_FUNCTIONS, entry.getValue(), attrValue, pa, attributes, 502);
		    			break;
		        	case "Синхронизация с телефоном":
		        		processListAttribute(GRP_ATTR_FUNCTIONS, entry.getValue(), attrValue, pa, attributes, 503);
		    			break;
		        	case "Управление входящими и исходящими вызовами":
		        		processListAttribute(GRP_ATTR_FUNCTIONS, entry.getValue(), attrValue, pa, attributes, 504);
		        		break;
		        	case "Управление входящими и исходящими SMS":
		        		processListAttribute(GRP_ATTR_FUNCTIONS, entry.getValue(), attrValue, pa, attributes, 505);
		        		break;
		        	case "Уведомления событий телефона (входящий звонок, SMS, соц. сети и др)":
		        		attributes.add(ProductAttributeBoolean.of(pa, GRP_ATTR_FUNCTIONS, "Уведомления событий телефона", "есть".equalsIgnoreCase(attrValue), 506));
		        		break;
		        	case "Тип оповещения":
		        		processListAttribute(GRP_ATTR_FUNCTIONS, entry.getValue(), attrValue, pa, attributes, 505);
		        		break;
		        	case "Громкая связь​":
		        		attributes.add(ProductAttributeBoolean.of(pa, GRP_ATTR_FUNCTIONS, entry.getValue(), "есть".equalsIgnoreCase(attrValue), 500));
		        		break;
		        	case "Съемный ремешок":
		        		attributes.add(ProductAttributeBoolean.of(pa, GRP_ATTR_CONSTRUCTION, entry.getValue(), "есть".equalsIgnoreCase(attrValue), 50));
		        		break;
		        	case "Влагозащита, защита от ударов":
					boolean def = !"нет".equalsIgnoreCase(attrValue);
					attributes.add(ProductAttributeBoolean.of(pa, GRP_ATTR_CONSTRUCTION, entry.getValue(), def, def ? attrValue : null, 201));
		        		break;
		        	case "Поддержка карт памяти":
		        		boolean exists = attrValue.startsWith("есть");
		        		attributes.add(ProductAttributeBoolean.of(pa, GRP_ATTR_CONSTRUCTION, entry.getValue(),
		        				exists, exists ? attrValue.substring(attrValue.lastIndexOf(", ") + 1).trim() : null, 202));
		        		break;
		        	case "Встроенная камера и кол-во точек":
	        			Matcher cameraMatcher = PATTERN_EXISTS_VALUES.matcher(attrValue);
		        		if (cameraMatcher.find()) {
		        			attributes.add(ProductAttributeNumeric.of(pa, GRP_ATTR_CONSTRUCTION, "Встроенная камера",
			        				new BigDecimal(cameraMatcher.group(1).replaceAll(",",  ".")), cameraMatcher.group(2), 203));
		        		}
		        		break;
		        	case "Размер экрана":
		        		attributes.add(ProductAttributeNumeric.of(pa, GRP_ATTR_SCREEN, entry.getValue(),
		        				new BigDecimal(attrValue.substring(1, attrValue.length() - 3).replaceAll(",", "")),
		        				"\"", 600));
		        	case "Тип экрана":
		        		Matcher screenMatcher = PATTERN_SCREEN.matcher(attrValue);
		        		if (screenMatcher.find()) {
		        			attributes.add(ProductAttributeBoolean.of(pa, GRP_ATTR_SCREEN, "Дисплей", screenMatcher.group(1) != null,
			        				screenMatcher.group(1), 601));
		        			attributes.add(ProductAttributeBoolean.of(pa, GRP_ATTR_SCREEN, "Сенсорный", screenMatcher.group(2) != null, 602));
		        			if (screenMatcher.group(3) != null) {
		        				attributes.add(ProductAttributeString.of(pa, GRP_ATTR_SCREEN, "Технология дисплея", screenMatcher.group(3), 603));
		        			}
		        			if (screenMatcher.group(4) != null) {
		        				attributes.add(ProductAttributeString.of(pa, GRP_ATTR_SCREEN, "Разрешение дисплея", screenMatcher.group(4), 603));
		        			}
		        		}
		        		break;
		        	default:
		        		break;
		        }
		    	log.info("Attr: " + entry.getValue() + "; value: "+ attrValue);
		    }
		}
		return attributes;
	}

	private void processImages(String[] columns, ProductArticle pa) {
	    pa.getImages().clear();
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
					mainImage.setName(pa.getArticle() + "-main");
					mainImage.setType("image/" + imgURL.substring(imgURL.lastIndexOf(".") + 1));
					imageRepo.save(mainImage);
					pa.setMainImage(mainImage);
				} else {
					Image img = new Image();
					img.setContent(IOUtils.toByteArray(imgIS));
					img.setName(pa.getArticle() + "-" + imgIndex);
					img.setType("image/" + imgURL.substring(imgURL.lastIndexOf(".") + 1));
					imageRepo.save(img);
					pa.getImages().add(img);
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
			if (str.startsWith(ATTRIBUTE)) {
                String attributeName = StringUtils.trim(str.substring(str.lastIndexOf(":") + 1));
                attributeMap.put(str, attributeName);
            }
		}
		
	}
	
	private String getProperty(String[] columns, String columnName) {
		Integer index = columnsMap.get(columnName);
		return index < columns.length ? columns[index] : null;
	}

	public static void main(String[] args) {
		Pattern p = Pattern.compile("(цветной)?\\s*(сенсорный)?\\s*(TFT|AMOLED|IPS|ЖК)?[- ]?дисплей?\\s*(.*)?");
		String str = "цветной сенсорный  TFT-дисплей  128х128";
		String str1 = "цветной сенсорный IPS-дисплей 320х240";
		String str2 = "цветной сенсорный AMOLED дисплей 400х400";
		String str3 = "ЖК-дисплей";
		Matcher m = p.matcher(str3);
		if (m.find()) {
			for (int group = 1; group <= m.groupCount(); group++) {
				System.out.println(m.group(group));
			}
		}
		
	}
	
}
