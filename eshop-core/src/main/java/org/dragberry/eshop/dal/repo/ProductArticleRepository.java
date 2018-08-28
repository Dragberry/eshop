package org.dragberry.eshop.dal.repo;

import org.dragberry.eshop.dal.entity.ProductArticle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductArticleRepository extends CrudRepository<ProductArticle, Long> {

}
