package org.dragberry.eshop.dal.repo;

import java.util.List;

import org.dragberry.eshop.dal.entity.Category;
import org.dragberry.eshop.dal.entity.ProductAttributeString;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAttributeStringRepository extends ProductAttributeRepository<String, ProductAttributeString> {

	@Query("select distinct attr.value from ProductAttributeString attr join attr.productArticle pa join pa.categories c where c = :category and attr.name = :attributeName order by attr.value")
	List<String> findByNameAndCategory(String attributeName, Category category);

}
