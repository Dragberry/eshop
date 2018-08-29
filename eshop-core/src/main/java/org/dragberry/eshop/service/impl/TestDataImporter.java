package org.dragberry.eshop.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.dragberry.eshop.dal.entity.Category;
import org.dragberry.eshop.dal.entity.Image;
import org.dragberry.eshop.dal.entity.Product;
import org.dragberry.eshop.dal.entity.Product.SaleStatus;
import org.dragberry.eshop.dal.entity.ProductArticle;
import org.dragberry.eshop.dal.entity.ProductArticleOption;
import org.dragberry.eshop.dal.repo.CategoryRepository;
import org.dragberry.eshop.dal.repo.ImageRepository;
import org.dragberry.eshop.dal.repo.ProductArticleOptionRepository;
import org.dragberry.eshop.dal.repo.ProductArticleRepository;
import org.dragberry.eshop.dal.repo.ProductRepository;
import org.dragberry.eshop.service.DataImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class TestDataImporter implements DataImporter {
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
    private ImageRepository imageRepo;
	
	@Autowired
    private ProductRepository productRepo;
	
	@Autowired
	private ProductArticleRepository productArticleRepo;

	@Autowired
	private ProductArticleOptionRepository productArticleOptionRepo;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
    @Override
    @Transactional
    public void importData(InputStream is) {
        log.info("Starting import...");
        try (Workbook wb = new HSSFWorkbook(is)) {
            wb.getSheetAt(0).rowIterator().forEachRemaining(row -> {
                String categoryName = row.getCell(0).getStringCellValue();
				if (row.getRowNum() > 0 && StringUtils.isNotBlank(categoryName)) {
					try {
						Category ctg = processCategory(categoryName);
						processProduct(row, ctg);
					} catch (RuntimeException re) {
						log.error(MessageFormat.format("An error has ooccured during importing data, row: {0}]", row.getRowNum()), re);
						throw re;
					}
                }
            });
            
        } catch (IOException exc) {
            log.error("An error has ooccured during importing data", exc);
        }
        log.info("Finishing import...");
    }

    private ProductArticle findOrCreateArticle(String article, String title, Category category, Row row) {
    	var pa = productArticleRepo.findByArticle(article);
		if (pa == null) {
			pa = new ProductArticle();
			pa.setArticle(article);
			pa.setTitle(title);
			pa.setCategories(List.of(category));
			pa.setReference(article);
			pa.setDescription(row.getCell(6).getStringCellValue());
			pa.setDescriptionFull(row.getCell(7).getStringCellValue());
			try {
			    InputStream is = resourceLoader.getResource(MessageFormat.format("classpath:data/{0}_main.png", article)).getInputStream();
			    Image img = new Image();
                img.setType("image/png");
                img.setName(MessageFormat.format("{0}_main.png", article));
                img.setContent(IOUtils.toByteArray(is));
                img = imageRepo.save(img);
			    pa.setMainImage(img);
			} catch (Exception exc) {
			    log.error(MessageFormat.format("An erro has occured during image importing, Row {0}", row.getRowNum()));
			}
			pa = productArticleRepo.save(pa);
		}
		log.info(MessageFormat.format("The product with the article '{0}' is already exists. Row: {1}", article, row.getRowNum()));
		return pa;
    }
    
    private void processProduct(Row row, Category category) {
		String article = row.getCell(3).getStringCellValue();
		String title = row.getCell(2).getStringCellValue();
		if (StringUtils.isNotBlank(article) && StringUtils.isNoneBlank(title)) {
			var pa = findOrCreateArticle(article, title, category, row);
			List<ProductArticleOption> options = Arrays.stream(row.getCell(9).getStringCellValue().split(";")).map(String::trim).map(color -> {
				ProductArticleOption pao = productArticleOptionRepo.findByProductArticleAndNameAndValue(pa, "Цвет", color);
				if (pao == null) {
					pao = new ProductArticleOption();
					pao.setProductArticle(pa);
					pao.setName("Цвет");
					pao.setValue(color);
					pao = productArticleOptionRepo.save(pao);
				}
				return pao;
			}).collect(Collectors.toList());
			
			if (options.isEmpty()) {
				Product p = new Product();
				p.setPrice(new BigDecimal(row.getCell(10).getNumericCellValue()));
				p.setActualPrice(new BigDecimal(row.getCell(10).getNumericCellValue() * 0.8));
				p.setProductArticle(pa);
				p.setQuantity(1);
				p.setSaleStatus(SaleStatus.EXPOSED);
				p = productRepo.save(p);
			} else {
				options.forEach(option -> {
					Product p = new Product();
					p.setPrice(new BigDecimal(row.getCell(10).getNumericCellValue()));
					p.setActualPrice(new BigDecimal(row.getCell(10).getNumericCellValue() * 0.8));
					p.setProductArticle(pa);
					p.setOptions(Set.of(option));
					p.setQuantity(1);	
					p.setSaleStatus(SaleStatus.EXPOSED);
					productRepo.save(p);
				});
				return;
			}
		}
		log.info(MessageFormat.format("The product article or product title is not provided in row {0}", row.getRowNum()));
	}

	/**
     * Process category
     * @param categoryName
     * @return category
     */
	private Category processCategory(String categoryName) {
		Category ctg = categoryRepo.findByName(categoryName);
		if (ctg == null) {
			ctg = new Category();
			ctg.setName(categoryName);
			ctg.setReference(categoryName);
			ctg = categoryRepo.save(ctg);
			log.info(MessageFormat.format("Category {0} is created", categoryName));
		} else {
			log.info(MessageFormat.format("Category {0} is found", categoryName));
		}
		return ctg;
	}

}
