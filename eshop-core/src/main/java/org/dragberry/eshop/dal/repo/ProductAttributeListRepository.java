package org.dragberry.eshop.dal.repo;

import java.util.List;

import org.dragberry.eshop.dal.entity.Category;
import org.dragberry.eshop.dal.entity.ProductAttributeList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAttributeListRepository extends ProductAttributeRepository<List<String>, ProductAttributeList> {

	@Query("select distinct v from ProductAttributeList attr join attr.value v join attr.productArticle pa join pa.categories c where c = :category and attr.name = :attributeName order by v")
	List<String> findByNameAndCategory(String attributeName, Category category);

	/**
     * Find values by the given string
     * @param value query
     * @return list of values
     */
    @Query("SELECT DISTINCT attr.value FROM ProductAttributeList attr JOIN attr.value v WHERE UPPER(attr.value) LIKE CONCAT('%', :value, '%')")
    List<String> findValues(String value);
}
