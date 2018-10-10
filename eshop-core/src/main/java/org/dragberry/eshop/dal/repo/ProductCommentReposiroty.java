package org.dragberry.eshop.dal.repo;

import java.util.List;

import org.dragberry.eshop.dal.dto.ProductCommentDTO;
import org.springframework.data.domain.Pageable;

/**
 * Interface of custom comment search
 * @author Maksim Dragun
 */
public interface ProductCommentReposiroty {

	/**
	 * This methods is used to search product comments on comment page
	 * @param page - {@link Pageable}
	 * @return list of {@link ProductCommentDTO}
	 */
	List<ProductCommentDTO> findProductComments(Pageable page);
}
