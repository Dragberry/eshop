package org.dragberry.eshop.dal.sort;

public interface SortConfig<R extends Roots> {
	
	SortFunction<R> getDefault();
	
	default SortFunction<R> get(String param) {
		return getDefault();
	}

}
