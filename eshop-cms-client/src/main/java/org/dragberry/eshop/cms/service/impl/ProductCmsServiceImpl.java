package org.dragberry.eshop.cms.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dragberry.eshop.cms.mapper.ProductMapper;
import org.dragberry.eshop.cms.model.ProductListItemTO;
import org.dragberry.eshop.cms.model.ProductArticleListItemTO;
import org.dragberry.eshop.cms.model.ProductCategoryTO;
import org.dragberry.eshop.cms.service.ProductCmsService;
import org.dragberry.eshop.common.PageableList;
import org.dragberry.eshop.dal.dto.ProductArticleListItemDTO;
import org.dragberry.eshop.dal.entity.Product;
import org.dragberry.eshop.dal.repo.CategoryRepository;
import org.dragberry.eshop.dal.repo.ProductArticleRepository;
import org.dragberry.eshop.dal.repo.ProductRepository;
import org.dragberry.eshop.dal.repo.ProductRepository.ProductOrderItemSpecification;
import org.dragberry.eshop.service.ImageService;
import org.dragberry.eshop.utils.ProductTitleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductCmsServiceImpl implements ProductCmsService {
    
	@Autowired
	private CategoryRepository categoryRepo;
	
    @Autowired
    private ProductArticleRepository productArticleRepo;
    
    @Autowired
    private ProductRepository productRepo;
    
    @Autowired
    private ImageService imageService;
    
    @Override
    public PageableList<ProductListItemTO> searchProducts(PageRequest pageRequest, Map<String, String[]> searchParams) {
    	Page<Product> page = productRepo.findAll(new ProductOrderItemSpecification(searchParams), pageRequest);
        return PageableList.of(page.stream().map(dto -> {
            ProductListItemTO to = new ProductListItemTO();
            to.setProductId(dto.getEntityKey());
            to.setProductArticleId(dto.getProductArticle().getEntityKey());
            to.setArticle(dto.getProductArticle().getArticle());
            to.setTitle(dto.getProductArticle().getTitle());
            to.setOptionsTitle(ProductTitleBuilder.buildOptionsTitle(dto));
            to.setPrice(dto.getPrice());
            if (!Objects.equals(dto.getPrice(), dto.getActualPrice())) {
                to.setActualPrice(dto.getActualPrice());
            }
            return to;
        }).collect(Collectors.toList()), page.getNumber() + 1, page.getSize(), 10, page.getTotalElements());
    }

    @Override
    public Optional<List<ProductListItemTO>> getProductOptions(Long productArticleId) {
        return productArticleRepo.findById(productArticleId).map(pa -> {
           return pa.getProducts().stream().map(product -> {
               return ProductMapper.map(product, imageService::findMainImage);
           }).collect(Collectors.toList());
        });
    }
    
    @Override
    public PageableList<ProductArticleListItemTO> getProducts(Integer pageNumber, Integer pageSize,
            Map<String, String[]> parameterMap) {
        Page<ProductArticleListItemDTO> page = productArticleRepo.search(PageRequest.of(pageNumber, pageSize), parameterMap);
        return PageableList.of(page.stream().map(entity -> {
            ProductArticleListItemTO item = new ProductArticleListItemTO();
            item.setId(entity.getId());
            item.setArticle(entity.getArticle());
            item.setTitle(entity.getTitle());
            item.setPrice(entity.getPrice());
            item.setActualPrice(entity.getActualPrice());
            item.setOptionsCount(entity.getOptionsCount());
            item.setMainImage(imageService.findMainImage(entity.getId(), entity.getArticle()));
            return item;
        }).collect(Collectors.toList()), page.getNumber() + 1, page.getSize(), page.getTotalPages(), page.getTotalElements());
    }
    
    @Override
    public List<ProductCategoryTO> getCategoryTree() {
    	ProductCategoryTO root = new ProductCategoryTO();
    	root.setId(-1L);
    	root.setName("common.catalog");
    	root.setCategories(categoryRepo.findAll().stream().map(ctg -> {
    		ProductCategoryTO ctgTO = new ProductCategoryTO();
    		ctgTO.setId(ctg.getEntityKey());
    		ctgTO.setName(ctg.getName());
    		return ctgTO;
    	}).collect(Collectors.toList()));
    	return List.of(root);
    }
}
