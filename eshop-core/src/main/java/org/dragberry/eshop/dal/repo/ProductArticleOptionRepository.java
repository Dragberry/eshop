package org.dragberry.eshop.dal.repo;

import org.dragberry.eshop.dal.entity.ProductArticle;
import org.dragberry.eshop.dal.entity.ProductArticleOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductArticleOptionRepository extends JpaRepository<ProductArticleOption, Long> {

	ProductArticleOption findByProductArticleAndNameAndValue(ProductArticle pa, String name, String value);

}
