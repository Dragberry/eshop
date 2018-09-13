package org.dragberry.eshop.dal.repo;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.dragberry.eshop.dal.entity.Category;
import org.dragberry.eshop.dal.entity.ProductArticleOption;
import org.dragberry.eshop.model.common.KeyValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	/**
	 * Find category by name
	 * @return
	 */
	Optional<Category> findByName(String name);
	
	/**
	 * Find categories as an ordered list
	 * @return
	 */
	List<Category> findAllByOrderByOrder();

	/**
	 * Find category by reference
	 * @param categoryReference
	 * @return
	 */
    Optional<Category> findByReference(String categoryReference);

    @Query("select new org.dragberry.eshop.model.common.KeyValue(o.name, o.value) from ProductArticle pa join pa.categories c join pa.products p join p.options o where c.entityKey = :categoryId")
    Set<KeyValue> getOptionFilters(Long categoryId);

}
