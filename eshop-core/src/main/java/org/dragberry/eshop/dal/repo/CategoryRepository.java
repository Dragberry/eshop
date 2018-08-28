package org.dragberry.eshop.dal.repo;

import org.dragberry.eshop.dal.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	/**
	 * Find category by name
	 * @return
	 */
	Category findByName(String name);

}
