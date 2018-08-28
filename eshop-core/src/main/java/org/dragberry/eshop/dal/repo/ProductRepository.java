package org.dragberry.eshop.dal.repo;

import org.dragberry.eshop.dal.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

}
