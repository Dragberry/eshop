package org.dragberry.eshop.dal.repo;

import org.dragberry.eshop.dal.entity.ProductArticleOption;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductArticleOptionRepository extends CrudRepository<ProductArticleOption, Long> {

}
