package org.dragberry.eshop.dal.sort;

import javax.persistence.criteria.CriteriaBuilder;

import lombok.AllArgsConstructor;

@AllArgsConstructor 
public class SortContext<R extends Roots> {
	public final CriteriaBuilder cb;
	public final R roots;
	
	public static <R extends Roots> SortContext<R> of(CriteriaBuilder cb, R roots) {
		return new SortContext<R>(cb, roots);
	}
}