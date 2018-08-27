package org.dragberry.eshop.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Results {
	
	private Results() {}
	
	public static <T> ResultTO<T> create(T object) {
		return new ResultTO<T>(object, Collections.emptyList());
	}
	
	public static <T> ResultTO<T> create(T object, List<IssueTO> issues) {
		return new ResultTO<T>(object, issues);
	}

	public static <T> ResultTO<T> create(T object, IssueTO... issues) {
		return new ResultTO<T>(object, Arrays.asList(issues));
	}
}
