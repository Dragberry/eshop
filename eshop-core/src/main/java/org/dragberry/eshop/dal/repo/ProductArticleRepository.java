package org.dragberry.eshop.dal.repo;

import java.util.List;
import java.util.Optional;

import org.dragberry.eshop.dal.entity.ProductArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductArticleRepository extends JpaRepository<ProductArticle, Long>, JpaSpecificationExecutor<ProductArticle> {

	/**
	 * Find by article
	 * @param article
	 * @return
	 */
	Optional<ProductArticle> findByArticle(String article);

	/**
	 * Find by category reference
	 * @param categoryReference
	 * @return
	 */
	@Query("select pa from ProductArticle pa join pa.categories c where c.reference = :categoryReference")
    List<ProductArticle> findByCategoryReference(String categoryReference);

	/**
	 * Find an entity by reference and category reference
	 * @param categoryReference
	 * @param productReference
	 * @return
	 */
	@Query("select pa from ProductArticle pa join pa.categories c where pa.reference = :productReference and c.reference = :categoryReference")
    ProductArticle findByReferenceAndCategoryReference(String categoryReference, String productReference);
    
    @Query("select pa.mainImage.entityKey from ProductArticle pa where pa.entityKey = :productArticleId")
    Long findMainImageKey(Long productArticleId);

}
