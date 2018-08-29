package org.dragberry.eshop.dal.repo;

import java.util.List;

import org.dragberry.eshop.dal.entity.ProductArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductArticleRepository extends JpaRepository<ProductArticle, Long> {

	/**
	 * Find by article
	 * @param article
	 * @return
	 */
	ProductArticle findByArticle(String article);

	/**
	 * Find by category reference
	 * @param categoryReference
	 * @return
	 */
	@Query("select pa from ProductArticle pa join pa.categories c where c.reference = :categoryReference")
    List<ProductArticle> findByCategoryReference(String categoryReference);

}
