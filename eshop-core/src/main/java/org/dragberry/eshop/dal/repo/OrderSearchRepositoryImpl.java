package org.dragberry.eshop.dal.repo;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import org.dragberry.eshop.dal.dto.OrderDTO;
import org.dragberry.eshop.dal.entity.Order;
import org.dragberry.eshop.dal.entity.Order_;
import org.dragberry.eshop.dal.entity.PaymentMethod;
import org.dragberry.eshop.dal.entity.PaymentMethod_;
import org.dragberry.eshop.dal.entity.ShippingMethod;
import org.dragberry.eshop.dal.entity.ShippingMethod_;
import org.dragberry.eshop.dal.sort.Roots;
import org.dragberry.eshop.dal.sort.SortContext;
import org.dragberry.eshop.dal.sort.SortFunction;
import org.dragberry.eshop.dal.sort.SortUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import lombok.AllArgsConstructor;

public class OrderSearchRepositoryImpl implements OrderSearchRepository {

	private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("^attribute\\[(.*?)\\]$");
	
	private static final Pattern FROM_PATTERN = Pattern.compile("^from\\[(.*?)\\]$");
	
	private static final Pattern TO_PATTERN = Pattern.compile("^to\\[(.*?)\\]$");
	
	private static final Pattern DATE_FROM_PATTERN = Pattern.compile("^dateFrom\\[(.*?)\\]$");
	
	private static final Pattern DATE_TO_PATTERN = Pattern.compile("^dateTo\\[(.*?)\\]$");
	
	@AllArgsConstructor(staticName = "of")
	private static class OrderRoots implements Roots {
		final Root<Order> order;
		final Join<Order, ShippingMethod> shippingMethod;
		final Join<Order, PaymentMethod> paymentMethod;
	}
	
	private static final Map<String, SortFunction<OrderRoots>> SORT_CONFIG = new HashMap<>();
	static {
		SORT_CONFIG.put("id", SortFunction.of(roots -> roots.order.get(Order_.entityKey)));
		SORT_CONFIG.put("date", SortFunction.of(roots -> roots.order.get(Order_.createdDate)));
		SORT_CONFIG.put("totalAmount", SortFunction.of(roots -> roots.order.get(Order_.totalAmount)));
		SORT_CONFIG.put("status", SortFunction.of(roots -> roots.order.get(Order_.orderStatus)));
		SORT_CONFIG.put("isPaid", SortFunction.of(roots -> roots.order.get(Order_.paid)));
		SORT_CONFIG.put("paymentMethod", SortFunction.of(roots -> roots.paymentMethod.get(PaymentMethod_.name)));
		SORT_CONFIG.put("shippingMethod", SortFunction.of(roots -> roots.shippingMethod.get(ShippingMethod_.name)));
		
	}
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Page<OrderDTO> search(Pageable pageRequest, Map<String, String[]> searchParams) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OrderDTO> query = cb.createQuery(OrderDTO.class);
		Root<Order> root = query.from(Order.class);
		Join<Order, ShippingMethod> shippingJoin = root.join(Order_.shippingMethod);
		Join<Order, PaymentMethod> paymentJoin = root.join(Order_.paymentMethod);
		query.distinct(true).multiselect(
				root.get(Order_.entityKey),
				root.get(Order_.createdDate),
				root.get(Order_.totalAmount),
				root.get(Order_.fullName),
				root.get(Order_.phone),
				root.get(Order_.address),
				root.get(Order_.comment),
				root.get(Order_.email),
				root.get(Order_.paid),
				shippingJoin.get(ShippingMethod_.entityKey),
				shippingJoin.get(ShippingMethod_.name),
				paymentJoin.get(PaymentMethod_.entityKey),
				paymentJoin.get(PaymentMethod_.name),
				root.get(Order_.orderStatus),
				root.get(Order_.version))
		.orderBy(SortUtils.sortBy(SORT_CONFIG, searchParams, SortContext.of(cb, OrderRoots.of(root, shippingJoin, paymentJoin)))
				.orElse(cb.desc(root.get(Order_.createdDate))));
		return new PageImpl<>(
				em.createQuery(query)
					.setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize())
					.setMaxResults(pageRequest.getPageSize())
					.getResultList(), pageRequest, 0);
	}

}
