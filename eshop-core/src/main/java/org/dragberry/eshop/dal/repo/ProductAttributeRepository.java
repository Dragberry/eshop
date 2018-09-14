package org.dragberry.eshop.dal.repo;

import org.dragberry.eshop.dal.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ProductAttributeRepository<V, T extends ProductAttribute<V>> extends JpaRepository<T, Long> {

}
