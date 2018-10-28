package org.dragberry.eshop.dal.repo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.dragberry.eshop.dal.dto.OrderDTO;
import org.dragberry.eshop.dal.entity.Order;
import org.dragberry.eshop.dal.entity.Order_;
import org.dragberry.eshop.dal.entity.PaymentMethod;
import org.dragberry.eshop.dal.entity.PaymentMethod_;
import org.dragberry.eshop.dal.entity.ShippingMethod;
import org.dragberry.eshop.dal.entity.ShippingMethod_;
import org.dragberry.eshop.dal.sort.Roots;
import org.dragberry.eshop.dal.sort.SortConfig;
import org.dragberry.eshop.dal.sort.SortContext;
import org.dragberry.eshop.dal.sort.SortFunction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import lombok.AllArgsConstructor;

public class OrderSearchRepositoryImpl implements OrderSearchRepository {

	@AllArgsConstructor(staticName = "of")
	private static class OrderRoots implements Roots {
		final Root<Order> order;
		final Join<Order, ShippingMethod> shippingMethod;
		final Join<Order, PaymentMethod> paymentMethod;
	}
	
	private static final SortConfig<OrderRoots> SORT_CONFIG = new SortConfig<OrderRoots>() {
		
		private final Map<String, SortFunction<OrderRoots>> config = new HashMap<>();
		{
			config.put("id", SortFunction.of(roots -> roots.order.get(Order_.entityKey)));
			config.put("date", SortFunction.of(roots -> roots.order.get(Order_.createdDate)));
			config.put("totalAmount", SortFunction.of(roots -> roots.order.get(Order_.totalAmount)));
			config.put("status", SortFunction.of(roots -> roots.order.get(Order_.orderStatus)));
			config.put("isPaid", SortFunction.of(roots -> roots.order.get(Order_.paid)));
			config.put("paymentMethod", SortFunction.of(roots -> roots.paymentMethod.get(PaymentMethod_.name)));
			config.put("shippingMethod", SortFunction.of(roots -> roots.shippingMethod.get(ShippingMethod_.name)));
		}
		
		@Override
		public SortFunction<OrderRoots> get(String param) {
			return config.get(param);
		}
		
		@Override
		public SortFunction<OrderRoots> getDefault() {
			return SortFunction.of(roots -> roots.order.get(Order_.createdDate), Direction.DESC);
		}
	};
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Page<OrderDTO> search(Pageable pageRequest, Map<String, String[]> searchParams) {
		return new OrderSearchQuery().search(pageRequest, searchParams);
	}
	
	private class OrderSearchQuery extends AbstractSearchQuery<OrderDTO, OrderRoots> {
		
		private final Root<Order> root;
		private final Join<Order, ShippingMethod> shippingJoin;
		private final Join<Order, PaymentMethod> paymentJoin;
		
		public OrderSearchQuery() {
			super(OrderDTO.class, em);
			this.root = query.from(Order.class);
			this.shippingJoin = root.join(Order_.shippingMethod);
			this.paymentJoin = root.join(Order_.paymentMethod);
		}
		
		@Override
		protected List<Selection<?>> getSelectionList() {
			return Arrays.asList(
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
					root.get(Order_.version));
		}
		
		@Override
		protected Optional<Predicate> where(Map<String, String[]> searchParams) {
			List<Predicate> predicates = new ArrayList<>();
			predicates.addAll(numericRange("totalAmount", root.get(Order_.totalAmount), searchParams));
			predicates.addAll(dateRange("date", root.get(Order_.createdDate), searchParams));
			return predicates.isEmpty() ? Optional.empty() : Optional.of(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		}
	
		@Override
		protected SortConfig<OrderRoots> getSortConfig() {
			return SORT_CONFIG;
		}
	
		@Override
		protected SortContext<OrderRoots> getSortContext() {
			return SortContext.of(cb, OrderRoots.of(root, shippingJoin, paymentJoin));
		}
	}
	
}
