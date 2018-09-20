package org.dragberry.eshop.dal.repo;

import java.util.List;

import org.dragberry.eshop.dal.entity.Category;
import org.dragberry.eshop.dal.entity.ProductAttributeBoolean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAttributeBooleanRepository extends ProductAttributeRepository<Boolean, ProductAttributeBoolean> {

	@Query("select distinct attr.name from ProductAttributeBoolean attr join attr.productArticle pa join pa.categories c where c = :category and attr.group = :attributeGroup order by attr.name")
	List<String> findByGroupAndCategory(String attributeGroup, Category category);
	
	@Query("select distinct attr.description from ProductAttributeBoolean attr join attr.productArticle pa join pa.categories c where c = :category and attr.description is not null and attr.name = :attributeName order by attr.description")
    List<String> findByNameAndCategory(String attributeName, Category category);

}
