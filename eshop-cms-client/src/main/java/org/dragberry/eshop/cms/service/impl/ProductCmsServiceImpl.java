package org.dragberry.eshop.cms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.dragberry.eshop.cms.mapper.ProductMapper;
import org.dragberry.eshop.cms.model.ProductListItemTO;
import org.dragberry.eshop.cms.model.ProductArticleDetailsTO;
import org.dragberry.eshop.cms.model.ProductArticleListItemTO;
import org.dragberry.eshop.cms.model.ProductCategoryTO;
import org.dragberry.eshop.cms.service.ProductCmsService;
import org.dragberry.eshop.common.IssueTO;
import org.dragberry.eshop.common.PageableList;
import org.dragberry.eshop.common.ResultTO;
import org.dragberry.eshop.common.Results;
import org.dragberry.eshop.dal.dto.ProductArticleListItemDTO;
import org.dragberry.eshop.dal.entity.Category;
import org.dragberry.eshop.dal.entity.File;
import org.dragberry.eshop.dal.entity.Product;
import org.dragberry.eshop.dal.entity.ProductArticle;
import org.dragberry.eshop.dal.entity.ProductAttribute;
import org.dragberry.eshop.dal.entity.ProductAttributeBoolean;
import org.dragberry.eshop.dal.entity.ProductAttributeList;
import org.dragberry.eshop.dal.entity.ProductAttributeNumeric;
import org.dragberry.eshop.dal.entity.ProductAttributeString;
import org.dragberry.eshop.dal.repo.CategoryRepository;
import org.dragberry.eshop.dal.repo.ProductArticleRepository;
import org.dragberry.eshop.dal.repo.ProductAttributeBooleanRepository;
import org.dragberry.eshop.dal.repo.ProductAttributeListRepository;
import org.dragberry.eshop.dal.repo.ProductAttributeNumericRepository;
import org.dragberry.eshop.dal.repo.ProductAttributeStringRepository;
import org.dragberry.eshop.dal.repo.ProductRepository;
import org.dragberry.eshop.dal.repo.ProductRepository.ProductOrderItemSpecification;
import org.dragberry.eshop.model.common.FileTO;
import org.dragberry.eshop.model.product.AttributeTO;
import org.dragberry.eshop.model.product.BooleanAttributeTO;
import org.dragberry.eshop.model.product.ListAttributeTO;
import org.dragberry.eshop.model.product.NumericAttributeTO;
import org.dragberry.eshop.model.product.StringAttributeTO;
import org.dragberry.eshop.utils.ProductTitleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ProductCmsServiceImpl implements ProductCmsService {
    
	@Autowired
	private CategoryRepository categoryRepo;
	
    @Autowired
    private ProductArticleRepository productArticleRepo;
    
    @Autowired
    private ProductRepository productRepo;
    
    @Autowired
    private ProductAttributeBooleanRepository paBooleanRepo;
    
    @Autowired
    private ProductAttributeListRepository paListRepo;
    
    @Autowired
    private ProductAttributeNumericRepository paNumericRepo;
    
    @Autowired
    private ProductAttributeStringRepository paStringRepo;
    
    @Value("${url.catalog}")
    private String urlCatalog;
    
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
           return pa.getProducts().stream().map(ProductMapper::map).collect(Collectors.toList());
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
            item.setMainImage(entity.getMainImage());
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
    
    @Override
    public Optional<ProductArticleDetailsTO> getProductArticleDetails(Long productArticleId) {
    	return productArticleRepo.findById(productArticleId).map(entity -> {
    		ProductArticleDetailsTO to = new ProductArticleDetailsTO();
    		to.setId(entity.getEntityKey());
    		to.setTitle(entity.getTitle());
    		to.setArticle(entity.getArticle());
    		to.setDescription(entity.getDescription());
    		to.setDescriptionFull(entity.getDescriptionFull());
    		to.setTagTitle(entity.getTagTitle());
    		to.setTagKeywords(entity.getTagKeywords());
    		to.setTagDescription(entity.getTagDescription());
    		to.setStatus(entity.getSaleStatus());
    		FileTO mainImage = mapFile(entity.getMainImage());
            to.setMainImageId(mainImage.getId());
            to.getImages().add(mainImage);
            to.getImages().addAll(entity.getImages().stream().map(ProductCmsServiceImpl::mapFile).collect(Collectors.toList()));
    		
    		ProductCategoryTO root = new ProductCategoryTO();
            root.setId(-1L);
            root.setName("common.catalog");
            root.getCategories().addAll(entity.getCategories().stream()
                    .map(ProductCmsServiceImpl::mapCategory).collect(Collectors.toList()));
            to.setMainCategoryId(entity.getCategory().getEntityKey());
            to.getCategoryTree().add(root);
            to.setReference(Stream.of(
    		        urlCatalog,
    		        entity.getCategory().getReference(),
    		        entity.getReference()).collect(Collectors.joining("/")));
            
            mapAttributes(entity, to);
    		return to;
    	});
    }
    
    private void mapAttributes(ProductArticle entity, ProductArticleDetailsTO to) {
        entity.getAttributes().forEach(attr -> {
            AttributeTO<?> attrTO = attr.buildTO();
            if (attrTO instanceof StringAttributeTO) {
                to.getStringAttributes().add((StringAttributeTO) attrTO);
            } else if (attrTO instanceof BooleanAttributeTO) {
                to.getBooleanAttributes().add((BooleanAttributeTO) attrTO);
            } else if (attrTO instanceof ListAttributeTO) {
                to.getListAttributes().add((ListAttributeTO) attrTO);
            } else if (attrTO instanceof NumericAttributeTO) {
                to.getNumericAttributes().add((NumericAttributeTO) attrTO);
            }
        });
    }
    
    private static ProductCategoryTO mapCategory(Category ctg) {
        ProductCategoryTO to = new ProductCategoryTO();
        to.setId(ctg.getEntityKey());
        to.setName(ctg.getName());
        to.setReferecence(ctg.getReference());
        return to;
    }
    
    private static FileTO mapFile(File file) {
        FileTO to = new FileTO();
        to.setId(file.getEntityKey());
        to.setPath(file.getPath());
        to.setName(file.getName());
        return to;
    }
    
    @Override
    public Optional<ResultTO<ProductArticleDetailsTO>> updateAttributes(Long productArticleId, ProductArticleDetailsTO product) {
        List<IssueTO> issues = new ArrayList<>();
        return productArticleRepo.findById(productArticleId).map(entity -> {
            entity.getAttributes().clear();
            ProductArticle pa = entity;
            entity.getAttributes().addAll(product.streamAttributes()
                    .map(attr -> attr.buildEntity(pa))
                    .collect(Collectors.toList())
            );
            entity = productArticleRepo.save(entity);
            ProductArticleDetailsTO to = new ProductArticleDetailsTO();
            to.setId(entity.getEntityKey());
            mapAttributes(entity, to);
            return Results.create(to, issues);
        });
    }
    
    @Override
    public List<String> findGroupsForAttributes(String group) {
    	return productArticleRepo.findGroupsForAttributes(group, PageRequest.of(0, 10));
    }
    
    @Override
    public List<String> findNamesForAttributes(String type, String name) {
        return productArticleRepo.findNamesForAttributes(resolveProductAttributeType(type), name);
    }
    
    @Override
    public List<String> findValuesForAttributes(String type, String query) {
        Class<? extends ProductAttribute<?>> clazz = resolveProductAttributeType(type);
        Pageable page = PageRequest.of(0, 10);
        if (ProductAttributeBoolean.class.isAssignableFrom(clazz)) {
			return paBooleanRepo.findValues(query, page);
        } else if (ProductAttributeList.class.isAssignableFrom(clazz)) {
            return paListRepo.findValues(query, page);
        } else if (ProductAttributeNumeric.class.isAssignableFrom(clazz)) {
            return paNumericRepo.findUnits(query, page);
        } else if (ProductAttributeString.class.isAssignableFrom(clazz)) {
            return paStringRepo.findValues(query, page);
        }
        throw new UnsupportedOperationException("Unknow product attribute type " + type);
    }
    
    /**
     * Trying to resolve {@link ProductAttribute} subclass from the given string
     * @param type
     * @return a subclass of {@link ProductAttribute}
     * @throws IllegalArgumentException, if the type is invalid
     */
    @SuppressWarnings("unchecked")
    private Class<? extends ProductAttribute<?>> resolveProductAttributeType(String type) {
        try {
            Class<?> attrType = Class.forName(type);
            if (ProductAttribute.class.isAssignableFrom(attrType)) {
               return (Class<? extends ProductAttribute<?>>) attrType;
            }
        } catch (ClassNotFoundException cnfe) {
            log.warn("Unknown product attribute type: {0}", type);
        }
        throw new IllegalArgumentException("Unknow product attribute type " + type);
    }
}
