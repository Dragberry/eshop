package org.dragberry.eshop.dal.repo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.dragberry.eshop.dal.dto.OrderDTO;
import org.dragberry.eshop.dal.entity.Order;
import org.dragberry.eshop.dal.entity.Order.OrderStatus;
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
    
    private static final String ID = "id";
    private static final String STATUS = "status";
    private static final String SHIPPING_METHOD = "shippingMethod";
    private static final String PAYMENT_METHOD = "paymentMethod";
    private static final String DATE = "date";
    private static final String TOTAL_AMOUNT = "totalAmount";
    private static final String IS_PAID = "isPaid";

	@AllArgsConstructor(staticName = "of")
	private static class OrderRoots implements Roots {
		final Root<Order> order;
		final Join<Order, ShippingMethod> shippingMethod;
		final Join<Order, PaymentMethod> paymentMethod;
	}
	
	private static final SortConfig<OrderRoots> SORT_CONFIG = new SortConfig<OrderRoots>() {
		
		private final Map<String, SortFunction<OrderRoots>> config = new HashMap<>();
		{
			config.put(ID, SortFunction.of(roots -> roots.order.get(Order_.entityKey)));
			config.put(DATE, SortFunction.of(roots -> roots.order.get(Order_.orderDate)));
			config.put(TOTAL_AMOUNT, SortFunction.of(roots -> roots.order.get(Order_.totalAmount)));
			config.put(STATUS, SortFunction.of(roots -> roots.order.get(Order_.orderStatus)));
			config.put(IS_PAID, SortFunction.of(roots -> roots.order.get(Order_.paid)));
			config.put(PAYMENT_METHOD, SortFunction.of(roots -> roots.paymentMethod.get(PaymentMethod_.name)));
			config.put(SHIPPING_METHOD, SortFunction.of(roots -> roots.shippingMethod.get(ShippingMethod_.name)));
		}
		
		@Override
		public SortFunction<OrderRoots> get(String param) {
			return config.get(param);
		}
		
		@Override
		public SortFunction<OrderRoots> getDefault() {
			return SortFunction.of(roots -> roots.order.get(Order_.orderDate), Direction.DESC);
		}
	};
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Page<OrderDTO> search(Pageable pageRequest, Map<String, String[]> searchParams) {
		return new OrderSearchQuery().search(pageRequest, searchParams);
	}
	
	private class OrderSearchQuery extends AbstractSearchQuery<OrderDTO, OrderRoots> {
		
		public OrderSearchQuery() {
			super(OrderDTO.class, em);
		}
		
		@Override
		protected OrderRoots getRoots(CriteriaQuery<?> query) {
		    Root<Order> root = query.from(Order.class);
            return OrderRoots.of(root, root.join(Order_.shippingMethod), root.join(Order_.paymentMethod));
		}
		
		@Override
		protected List<Selection<?>> getSelectionList() {
			return Arrays.asList(
			        roots.order.get(Order_.entityKey),
					roots.order.get(Order_.orderDate),
					roots.order.get(Order_.totalAmount),
					roots.order.get(Order_.fullName),
					roots.order.get(Order_.phone),
					roots.order.get(Order_.address),
					roots.order.get(Order_.comment),
					roots.order.get(Order_.email),
					roots.order.get(Order_.paid),
					roots.shippingMethod.get(ShippingMethod_.entityKey),
					roots.shippingMethod.get(ShippingMethod_.name),
					roots.paymentMethod.get(PaymentMethod_.entityKey),
					roots.paymentMethod.get(PaymentMethod_.name),
					roots.order.get(Order_.orderStatus),
					roots.order.get(Order_.version));
		}
		
		@Override
		protected Expression<?> getCountExpression() {
		    return countRoots.order.get(Order_.entityKey);
		}
		
		@Override
		protected Optional<Predicate> where(Map<String, String[]> searchParams, OrderRoots roots) {
			List<Predicate> predicates = new ArrayList<>();
			predicates.addAll(numericRange(TOTAL_AMOUNT, roots.order.get(Order_.totalAmount), searchParams));
			predicates.addAll(dateRange(DATE, roots.order.get(Order_.createdDate), searchParams));
			predicates.addAll(inLong(PAYMENT_METHOD, roots.paymentMethod.get(PaymentMethod_.entityKey), searchParams));
			predicates.addAll(inLong(SHIPPING_METHOD, roots.shippingMethod.get(ShippingMethod_.entityKey), searchParams));
			predicates.addAll(inEnum(STATUS, roots.order.get(Order_.orderStatus), OrderStatus.class, searchParams));
			predicates.addAll(inBoolean(IS_PAID, roots.order.get(Order_.paid), searchParams));
			return predicates.isEmpty() ? Optional.empty() : Optional.of(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		}

		@Override
		protected SortConfig<OrderRoots> getSortConfig() {
			return SORT_CONFIG;
		}
	
		@Override
		protected SortContext<OrderRoots> getSortContext() {
			return SortContext.of(cb, roots);
		}
	}
	
}
