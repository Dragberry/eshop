package org.dragberry.eshop.dal.sort;

import java.util.Optional;
import java.util.function.Function;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;

import org.springframework.data.domain.Sort.Direction;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SortFunction<R extends Roots> {
	
	public final Function<SortContext<R>, Expression<?>> function;
	
	public final Optional<Direction> defaultDirection; 
	
	public static <R extends Roots> SortFunction<R> of(Function<SortContext<R>, Expression<?>> function) {
		return new SortFunction<R>(function, Optional.empty());
	}

	public static <R extends Roots> SortFunction<R> of(Function<SortContext<R>, Expression<?>> function, Direction direction) {
		return new SortFunction<R>(function, Optional.ofNullable(direction));
	}
	
	private Optional<Order> getOrder(SortContext<R> context, Optional<Direction> direction) {
		return direction.map(dir -> {
			switch (dir) {
			case ASC:
				return context.cb.asc(function.apply(context));
			case DESC:
				return context.cb.desc(function.apply(context));
			default:
				return null;
			}
		});
	}
	
	public Optional<Order> getOrder(SortContext<R> context) {
		return getOrder(context, defaultDirection);
	}
	
	public Optional<Order> getOrder(SortContext<R> context, String direction) {
		return getOrder(context, Direction.fromOptionalString(direction));
	}
	
}