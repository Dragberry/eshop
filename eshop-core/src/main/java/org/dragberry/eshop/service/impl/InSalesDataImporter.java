package org.dragberry.eshop.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dragberry.eshop.dal.entity.Category;
import org.dragberry.eshop.dal.entity.Comment;
import org.dragberry.eshop.dal.entity.Product;
import org.dragberry.eshop.dal.entity.ProductArticle;
import org.dragberry.eshop.dal.entity.ProductArticle.SaleStatus;
import org.dragberry.eshop.dal.entity.ProductArticleOption;
import org.dragberry.eshop.dal.entity.ProductAttribute;
import org.dragberry.eshop.dal.entity.ProductAttributeBoolean;
import org.dragberry.eshop.dal.entity.ProductAttributeList;
import org.dragberry.eshop.dal.entity.ProductAttributeNumeric;
import org.dragberry.eshop.dal.entity.ProductAttributeString;
import org.dragberry.eshop.dal.entity.ProductLabelType;
import org.dragberry.eshop.dal.repo.CategoryRepository;
import org.dragberry.eshop.dal.repo.ProductArticleOptionRepository;
import org.dragberry.eshop.dal.repo.ProductArticleRepository;
import org.dragberry.eshop.dal.repo.ProductRepository;
import org.dragberry.eshop.service.DataImporter;
import org.dragberry.eshop.service.ImageService;
import org.dragberry.eshop.service.TransliteService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j;

@Log4j
@Service("InSalesDataImporter")
@DependsOn("webAppInitializer")
public class InSalesDataImporter implements DataImporter {

	private static final String PRODUCT_ARTICLE = "Артикул";
	
	private static final String PRODUCT_DESCRIPTION = "Краткое описание";
	
	private static final String PRODUCT_DESCRIPTION_FULL = "Полное описание";
	
	private static final String PRODUCT_TITLE = "Название товара";

	private static final String PRODUCT_REFERENCE = "Название товара в URL";

	private static final String TAG_TITLE = "Тег title";
	
	private static final String TAG_KEYWORDS = "Мета-тег keywords";
	
	private static final String TAG_DESCRIPTION = "Мета-тег description";

	private static final String URL = "URL";
	
	private static final String ACTUAL_PRICE = "Цена продажи";
	
	private static final String PRICE = "Старая цена";
	
	private static final String IMAGES = "Изображения";
	
	private static final String CATEGORIES = "Размещение на сайте";

	private static final String QUANTITY = "Остаток";
	
	private static final String OPTION = "Свойство:";
	
	private static final String ATTRIBUTE = "Параметр:";
	
	private static final String LABEL = "Параметр: Метка";
	
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
    private ResourceLoader resourceLoader;
	
	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private ProductArticleRepository productArticleRepo;
	
	@Autowired
	private ProductArticleOptionRepository paoRepo;
	
	@Autowired
	private TransliteService transliteService;
	
	@Autowired
	private ImageService imageService;
	
	private Map<String, Integer> columnsMap = new HashMap<>();
	
	private Map<String, String> optionsMap = new HashMap<>();
	
	private Map<String, String> attributeMap = new HashMap<>();
	
	private Map<String, Category> categoryMap = new LinkedHashMap<>();
	
	@PostConstruct
	public void init() {
		categoryMap.put("Каталог смарт-часов", categoryRepo.findByName("Смарт-часы").get());
		categoryMap.put("Каталог фитнес-браслетов", categoryRepo.findByName("Фитнес-браслеты").get());
		categoryMap.put("Прочие аксессуары", categoryRepo.findByName("Прочие аксессуары").get());
		categoryMap.put("Каталог детских смарт-часов", categoryRepo.findByName("Детские смарт-часы").get());
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
	public void processRawArticle(String article, List<String[]> rawArticle) throws IOException {
		if (!rawArticle.isEmpty()) {
			ProductArticle pa = productArticleRepo.findByArticle(article).orElse(new ProductArticle());
			String[] firstLine = rawArticle.get(0);
			pa.setArticle(article);
			pa.setTitle(firstLine[columnsMap.get(PRODUCT_TITLE)]);
			pa.setReference(transliteService.transformToId(firstLine[columnsMap.get(PRODUCT_REFERENCE)]));
			pa.setDescription(processDescription(article, firstLine));
			pa.setDescriptionFull(processDescriptionFull(article, firstLine));
			pa.setTagTitle(firstLine[columnsMap.get(TAG_TITLE)]);
			pa.setTagKeywords(firstLine[columnsMap.get(TAG_KEYWORDS)]);
			pa.setTagDescription(firstLine[columnsMap.get(TAG_DESCRIPTION)]);
			pa.setSaleStatus(SaleStatus.EXPOSED);
			
			pa.setLabels(processLabels(firstLine));
			
			List<ProductAttribute<?>> attributes = processAttributes(pa, firstLine);
			pa.getAttributes().clear();
			if (!attributes.isEmpty()) {
				productArticleRepo.flush();
				pa.getAttributes().addAll(attributes);
			}
			
			for (String[] line : rawArticle) {
				pa.setCategories(new ArrayList<>());
				String categoryLine = getProperty(line, CATEGORIES);
				for (Map.Entry<String, Category> entry : categoryMap.entrySet()) {
					if (categoryLine.contains(entry.getKey())) {
						pa.getCategories().add(entry.getValue());
					}
				}
			}
			pa.setCategory(pa.getCategories().get(0));
			
			productRepo.deleteAll(pa.getProducts());
			pa.getProducts().clear();
            pa = productArticleRepo.save(pa);
            
            processComments(pa, firstLine);
			pa = productArticleRepo.save(pa);
            
			List<Product> productList = new ArrayList<>();
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
				productList.add(productRepo.save(product));
			}
			processImages(firstLine, pa, productList);
		}
		
	}

	private void processComments(ProductArticle pa, String[] firstLine) {
		pa.getComments().clear();
		String url = firstLine[columnsMap.get(URL)];
		try {
    		Document doc = Jsoup.parse(new URL(url), 10000);
    		for (Element el : doc.getElementsByClass("reviews-item")) {
    			Comment comment = new Comment();
    			comment.setUserIP("127.0.0.1");
    			comment.setStatus(Comment.Status.ACTIVE);
    			comment.setUserName(el.getElementsByClass("author").text());
    			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    			comment.setDateTime(formatter.parse(el.getElementsByClass("date").text(), LocalDateTime::from));
    			comment.setText(el.getElementsByClass("text").text());
    			Elements stars = el.getElementsByClass("star-item");
    			int mark = 5;
    			for (int index = 0; index < stars.size(); index++) {
    				if (stars.get(index).hasClass("active")) {
    					break;
    				}
    				mark--;
    			}
    			pa.addComment(comment, mark);
    		}
		} catch (Exception e) {
            log.warn("An error has occured during comments processing on " + url, e);
        }
	}

	private String processDescription(String article, String[] firstLine) {
		try (InputStream is = resourceLoader.getResource(MessageFormat.format("classpath:data/{0}/{0}_description.html", article)).getInputStream()) {
			Document description = Jsoup.parse(is, StandardCharsets.UTF_8.name(), StringUtils.EMPTY);
			return description.body().html();
		} catch (Exception e) {
			log.warn("Unable to parse description file for product " + article);
			return firstLine[columnsMap.get(PRODUCT_DESCRIPTION)];
		}
	}
	
	private String processDescriptionFull(String article, String[] firstLine) throws IOException {
		try (InputStream is = resourceLoader.getResource(MessageFormat.format("classpath:data/{0}/{0}_descriptionFull.html", article)).getInputStream()) {
			Document description = Jsoup.parse(is, StandardCharsets.UTF_8.name(), StringUtils.EMPTY);
			Elements links = description.select("img[src]");
		    	for (Element link : links)  {
		    		String imgURL = link.attr("src");
		            if (StringUtils.startsWith(imgURL, "https://static-eu.insales.ru/")) {
		            	int lastIndexOfSlash = imgURL.lastIndexOf("/");
		                String realURL = imgURL.substring(0, lastIndexOfSlash + 1) + URLEncoder.encode(imgURL.substring(lastIndexOfSlash + 1), StandardCharsets.UTF_8.name());
		                try (InputStream imgIS = new URL(realURL).openConnection().getInputStream()) {
		                    String imageName = imgURL.substring(imgURL.lastIndexOf("/") + 1);
		                    link.attr("src", imageService.createImage(imageName, imgIS));
		                }
		            }
		        }
			return description.body().html();
		} catch (Exception e) {
			log.warn("Unable to parse description file for product " + article);
		    Whitelist wl = Whitelist.simpleText();
		    wl.addTags("div", "span", "table", "tr", "th", "td", "li", "ul", "ol");
		    wl.addAttributes("img", "src");
	        Document doc = Jsoup.parseBodyFragment(Jsoup.clean(firstLine[columnsMap.get(PRODUCT_DESCRIPTION_FULL)], wl));
	        Elements links = doc.select("img[src]");
	        for (Element link : links)  {
	            String imgURL = link.attr("src");
	            if (StringUtils.isNotBlank(imgURL)) {
	            	int lastIndexOfSlash = imgURL.lastIndexOf("/");
	                String realURL = imgURL.substring(0, lastIndexOfSlash + 1) + URLEncoder.encode(imgURL.substring(lastIndexOfSlash + 1), StandardCharsets.UTF_8.name());
	                try (InputStream imgIS = new URL(realURL).openConnection().getInputStream()) {
	                    String imageName = imgURL.substring(imgURL.lastIndexOf("/") + 1);
	                    link.attr("src", imageService.createImage(imageName, imgIS));
	                    link.attr("class", "img-external");
	                }
	            }
	        }
	        return doc.html();
		}
	}
	
	private void processImages(String[] columns, ProductArticle pa, List<Product> products) throws IOException {
		String[] imgs = columns[columnsMap.get(IMAGES)].split(" ");
		imageService.deleteProductImages(pa.getEntityKey(), pa.getArticle());
        for (int imgIndex = 0; imgIndex < imgs.length; imgIndex++) {
            String imgURL = imgs[imgIndex];
            if (StringUtils.isBlank(imgURL)) {
            	continue;
            }
            log.info(MessageFormat.format("Image: {0}", imgURL));
            int lastIndexOfSlash = imgURL.lastIndexOf("/");
            String realURL = imgURL.substring(0, lastIndexOfSlash + 1) + URLEncoder.encode(imgURL.substring(lastIndexOfSlash + 1), StandardCharsets.UTF_8.name());
            String imageName = getImageName(pa, products, imgURL, imgIndex == 0);
            try (InputStream imgIS = new URL(realURL).openConnection().getInputStream()) {
                imageService.createProductImage(pa.getEntityKey(), pa.getArticle(), imageName, imgIS);
            }
        }
	}

    private String getImageName(ProductArticle pa, List<Product> products, String imgURL, boolean first) {
    	int dotIndex = imgURL.lastIndexOf(".");
		String imageExt = imgURL.substring(dotIndex + 1);
		if (first) {
			return MessageFormat.format("{0}_main.{1}", pa.getArticle(), imageExt);
		}
    	for (Product p : products) {
        	for (ProductArticleOption opt : p.getOptions()) {
        		log.info(opt.getValue().replaceAll(" ", ""));
        		if (StringUtils.containsIgnoreCase(imgURL, opt.getValue().replaceAll(" ", ""))) {
        			return MessageFormat.format("{0}_{1}_{2}.{3}", pa.getArticle(), opt.getName(), opt.getValue(), imageExt);
        		}
        	}
        }
		String oldImgName = imgURL.substring(imgURL.lastIndexOf("/") + 1, dotIndex);
		return MessageFormat.format("{0}_{1}.{2}", pa.getArticle(), oldImgName, imageExt) ;
	}

	private Map<String, ProductLabelType> processLabels(String[] firstLine) {
        return Arrays.stream(getProperty(firstLine, LABEL).split("##")).filter(StringUtils::isNotBlank).collect(Collectors.toMap(lbl -> {
        	if ("новое поступление".equals(lbl)) {
        		return "Новинка";
        	}
        	return StringUtils.capitalize(lbl);
        }, lbl -> {
        	switch (lbl) {
        	case "Скидка":
        		return ProductLabelType.A;
        	case "Хит продаж":
        		return ProductLabelType.B;
        	case "новое поступление":
        		return ProductLabelType.C;
        	default:
        		return ProductLabelType.C;
        	}
        }));
    }
	
	private static void processListAttribute(String group, String name, String attrValue, ProductArticle pa, List<ProductAttribute<?>> attributes, Integer order) {
		String[] values = attrValue.split("##");
		if (values.length > 0) {
			List<String> valueList = new ArrayList<>();
			valueList.addAll(Arrays.asList(values));
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
		return index < columns.length ? columns[index] : StringUtils.EMPTY;
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
