package org.dragberry.eshop.dal.repo;

import java.util.Optional;

import org.dragberry.eshop.dal.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {

    /**
     * Find a page by URL reference
     * @param reference
     * @return
     */
    Optional<Page> findByReference(String reference);
    
    /**
     * Find a page by viewName
     * @param viewName
     * @return
     */
    Optional<Page> findByViewName(String viewName);
    
    /**
     * Checks whether the page exists with such URL reference
     * @param reference
     * @return
     */
    boolean existsByReference(String reference);
}
