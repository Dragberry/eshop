package org.dragberry.eshop.dal.repo;

import org.dragberry.eshop.dal.entity.ProductArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductArticleRepository extends JpaRepository<ProductArticle, Long> {

	/**
	 * Find by article
	 * @param article
	 * @return
	 */
	ProductArticle findByArticle(String article);

}
