package org.dragberry.eshop.dal.sort;

import java.util.Optional;
import java.util.function.Function;

import javax.persistence.criteria.Expression;

import org.springframework.data.domain.Sort.Direction;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SortFunction<R extends Roots> {
	
	public final Function<R, Expression<?>> function;
	
	public static <R extends Roots> SortFunction<R> of(Function<R, Expression<?>> function) {
		return new SortFunction<R>(function);
	}
	
	public Optional<javax.persistence.criteria.Order> getOrder(SortContext<R> context, String direction) {
		return Direction.fromOptionalString(direction).map(dir -> {
			switch (dir) {
			case ASC:
				return context.cb.asc(function.apply(context.roots));
			case DESC:
				return context.cb.desc(function.apply(context.roots));
			default:
				return null;
			}
		});
	}
	
}