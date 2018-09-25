package org.dragberry.eshop.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultTO<T> implements Serializable {

	private static final long serialVersionUID = -3844249336933073185L;

	private final T value;
	
	private final List<IssueTO> issues;
	
	public ResultTO(T object, List<IssueTO> issues) {
		this.value = object;
		this.issues = issues != null ? issues : new ArrayList<>();
	}

	public T getValue() {
		return value;
	}

	public List<IssueTO> getIssues() {
		return issues;
	}
	
	public boolean hasIssues() {
		return !issues.isEmpty();
	}
	
}
