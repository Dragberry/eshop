package org.dragberry.eshop.dal.repo;

import java.math.BigDecimal;
import java.util.List;

import org.dragberry.eshop.dal.entity.Category;
import org.dragberry.eshop.dal.entity.ProductAttributeNumeric;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAttributeNumericRepository extends ProductAttributeRepository<BigDecimal, ProductAttributeNumeric> {

    @Query("select distinct attr.unit from ProductAttributeNumeric attr join attr.productArticle pa join pa.categories c where c = :category and attr.unit is not null and attr.name = :attributeName order by attr.unit")
    List<String> getUnits(String attributeName, Category category);

    /**
     * Find units by the given string
     * @param unit query
     * @param page
     * @return list of units
     */
    @Query("SELECT DISTINCT attr.unit FROM ProductAttributeNumeric attr WHERE UPPER(attr.unit) LIKE CONCAT('%', :unit, '%')")
    List<String> findUnits(String unit, Pageable page);
}
