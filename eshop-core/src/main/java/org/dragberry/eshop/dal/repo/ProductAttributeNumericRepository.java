package org.dragberry.eshop.dal.repo;

import org.dragberry.eshop.dal.entity.ProductAttributeBoolean;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAttributeNumericRepository extends ProductAttributeRepository<Boolean, ProductAttributeBoolean> {

}
