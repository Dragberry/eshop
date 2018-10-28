package org.dragberry.eshop.dal.repo;

import java.util.Map;

import org.dragberry.eshop.dal.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderSearchRepository {

	Page<OrderDTO> search(Pageable page, Map<String, String[]> searchParams);
}
