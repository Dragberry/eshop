package org.dragberry.eshop.dal.sort;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SortUtils {

private static final Pattern SORT_PATTERN = Pattern.compile("^(.*?)\\[(.*?)\\]$");
	
	private static final String SORT_PARAM = "sort";
	
	private SortUtils() {}
	
	public static <R extends Roots> Optional<javax.persistence.criteria.Order> orderBy(
			Map<String, SortFunction<R>> sortConfig,
			Map<String, String[]> searchParams, SortContext<R> ctx) {
		String[] values = searchParams.get(SORT_PARAM);
		if (values != null && values.length > 0) {
			Matcher m = SORT_PATTERN.matcher(values[0]);
			if (m.matches()) {
				return sortConfig.get(m.group(1)).getOrder(ctx, m.group(2));
			}
		}
		return Optional.empty();
	}
}
