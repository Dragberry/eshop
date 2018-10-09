package org.dragberry.eshop.application;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.dragberry.eshop.dal.entity.Category;
import org.dragberry.eshop.dal.entity.CategoryFilter;
import org.dragberry.eshop.dal.entity.CategoryFilterAllBoolean;
import org.dragberry.eshop.dal.entity.CategoryFilterAllList;
import org.dragberry.eshop.dal.entity.CategoryFilterAnyBoolean;
import org.dragberry.eshop.dal.entity.CategoryFilterAnyString;
import org.dragberry.eshop.dal.entity.CategoryFilterRange;
import org.dragberry.eshop.dal.entity.MenuPage;
import org.dragberry.eshop.dal.entity.Page;
import org.dragberry.eshop.dal.repo.CategoryRepository;
import org.dragberry.eshop.dal.repo.MenuPageRepository;
import org.dragberry.eshop.dal.repo.PageRepository;
import org.dragberry.eshop.service.ImageService;
import org.dragberry.eshop.service.TransliteService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class WebAppInitializer {

	@Autowired
    private ResourceLoader resourceLoader;
	
	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
    private MenuPageRepository menuPageRepo;
	
	@Autowired
	private PageRepository pageRepo;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private TransliteService transliteService;
	
	@PostConstruct
	public void init() {
		if (!pageRepo.existsByReference("/")) {
			createPages();
		}
		if (!categoryRepo.findByName("Смарт-часы").isPresent()) {
			createCategories();
		}
	}

	private void createCategories() {
		createCategory("Смарт-часы", 0, this::smartWatchFilters);
		createCategory("Фитнес-браслеты", 2, ctg -> Collections.emptyList());
		createCategory("Прочие аксессуары", 3, ctg -> Collections.emptyList());
		createCategory("Детские смарт-часы", 1, this::smartWatchFilters);
	}
	
	private List<CategoryFilter<?, ?, ?>> smartWatchFilters(Category ctg) {
		List<CategoryFilter<?,?,?>> filters = new ArrayList<>();
		CategoryFilterAnyString tech = new CategoryFilterAnyString();
		tech.setCategory(ctg);
		tech.setName("Технология дисплея");
		tech.setOrder(0);
		filters.add(tech);
		CategoryFilterAnyString dim = new CategoryFilterAnyString();
		dim.setCategory(ctg);
		dim.setName("Разрешение дисплея");
		dim.setOrder(1);
		filters.add(dim);
		CategoryFilterAllList ff = new CategoryFilterAllList();
		ff.setCategory(ctg);
		ff.setName("Фитнес-функции");
		ff.setOrder(2);
		filters.add(ff);
		ctg.setFilters(filters);
		CategoryFilterAllList aps = new CategoryFilterAllList();
		aps.setCategory(ctg);
		aps.setName("Встроенные приложения");
		aps.setOrder(3);
		filters.add(aps);
		CategoryFilterAllBoolean ifs = new CategoryFilterAllBoolean();
		ifs.setCategory(ctg);
		ifs.setName("Интерфейсы");
		ifs.setOrder(4);
		filters.add(ifs);
		CategoryFilterAnyBoolean sim = new CategoryFilterAnyBoolean();
        sim.setCategory(ctg);
        sim.setName("Поддержка SIM-карты");
        sim.setOrder(5);
        filters.add(sim);
        CategoryFilterRange weight = new CategoryFilterRange();
        weight.setCategory(ctg);
        weight.setName("Вес");
        weight.setOrder(6);
        filters.add(weight);
		return filters;
	}
	
	/**
     * Process category
     * @param name
     * @param order
     * @param filtersProvider
     */
	private void createCategory(String name, int order, Function<Category, List<CategoryFilter<?, ?, ?>>> filtersProvider) {
		Category ctg = new Category();
		ctg.setName(name);
		ctg.setReference(transliteService.transformToId(name));
		ctg.setOrder(order);
		if (filtersProvider != null) {
			ctg.setFilters(filtersProvider.apply(ctg));
		}
		categoryRepo.save(ctg);
	}
	
	private void createPages() {
	    createPage("/", "Главная страница");
	    
	    createPage("/dostavka", "Доставка", "Доставка", 1);
	    createPage("/oplata-i-rassrochka", "Оплата и рассрочка", "Оплата и рассрочка", 2);
	    createPage("/garantiya-i-servis", "Гарантия и сервис", "Гарантия и сервис", 3);
	    createPage("/kontakty", "Контакты", "Контакты", 4);
	    createPage("/otzyvy-pokupatelei", "Отзывы покупателей", "Отзывы покупателей", 5);
	    createPage("/optovaya-torgovlya", "Оптовая торговля", "Оптовая торговля", 1);
	}
	
	private void createPage(String reference, String name) {
	    createPage(reference, name, null, null);
    }
	
	private void createPage(String reference, String title, String breadcrumbTitle, Integer order) {
	    pageRepo.findByReference(reference).orElseGet(() -> {
            Page page = new  Page();
            page.setReference(reference);
            page.setTitle(title);
            page.setBreadcrumbTitle(breadcrumbTitle);
            try (InputStream is = resourceLoader.getResource("classpath:templates/pages" + ("/".equals(reference) ? "/home" : reference) + ".html").getInputStream()) {
            	Document description = Jsoup.parse(is, StandardCharsets.UTF_8.name(), StringUtils.EMPTY);
		    	for (Element img : description.select("img[src]"))  {
		            createImageAndReplaceLink(img, "src");
		        }
                for (Element link : description.select("a[href]"))  {
                    createImageAndReplaceLink(link, "href");
                }
            	page.setContent(description.html());
            } catch (Exception e) {
                log.warn("Unable to open " + reference + " page");
            }
            page = pageRepo.save(page);
            if (order != null) {
                MenuPage mp = new MenuPage();
                mp.setPage(page);
                mp.setOrder(order);
                mp.setStatus(MenuPage.Status.ACTIVE);
                menuPageRepo.save(mp);
            }
            return page;
        });
	}

    private void createImageAndReplaceLink(Element link, String attr) throws UnsupportedEncodingException, IOException, MalformedURLException {
        String imgURL = link.attr(attr);
        if (StringUtils.startsWith(imgURL, "https://static-eu.insales.ru/")) {
            int lastIndexOfSlash = imgURL.lastIndexOf("/");
            String realURL = imgURL.substring(0, lastIndexOfSlash + 1) + URLEncoder.encode(imgURL.substring(lastIndexOfSlash + 1), StandardCharsets.UTF_8.name());
            try (InputStream imgIS = new URL(realURL).openConnection().getInputStream()) {
                String imageName = imgURL.substring(imgURL.lastIndexOf("/") + 1);
                link.attr(attr, imageService.createImage(imageName, imgIS));
            }
        }
    }
}
