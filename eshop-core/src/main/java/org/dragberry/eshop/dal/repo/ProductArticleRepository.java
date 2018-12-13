package org.dragberry.eshop.dal.repo;

import java.util.List;
import java.util.Optional;
import org.dragberry.eshop.dal.entity.ProductArticle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductArticleRepository extends ProductArticleSearchRepository, JpaRepository<ProductArticle, Long> {

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
	@Query("select pa from ProductArticle pa join pa.categories c left join fetch pa.attributes attr where pa.reference = :productReference and c.reference = :categoryReference")
    ProductArticle findByReferenceAndCategoryReference(String categoryReference, String productReference);
    
    /**
     * Find all labels for the given product article
     * @param productArticleId
     * @return
     */
    @Query("select entry(l) from ProductArticle pa join pa.labels l where pa.entityKey = :productArticleId")
	List<Object> findLabels(Long productArticleId);
    
    /**
     * This query is used to quick search attribute's groups
     * @param group
     * @param page
     * @return
     */
    @Query("SELECT DISTINCT pa.group FROM ProductAttribute pa WHERE UPPER(pa.group) LIKE CONCAT('%', :group, '%')")
	List<String> findGroupsForAttributes(String group, Pageable page);

}
