package org.dragberry.eshop.dal.repo;

import java.util.List;
import java.util.Optional;

import org.dragberry.eshop.dal.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	/**
	 * Find category by name
	 * @return
	 */
	Optional<Category> findByName(String name);
	
	/**
	 * Find categories as an ordered list
	 * @return
	 */
	List<Category> findAllByOrderByOrder();

}
