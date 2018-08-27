package org.dragberry.eshop.common;

import java.io.Serializable;
import java.util.List;

public class ResultTO<T> implements Serializable {

	private static final long serialVersionUID = -3844249336933073185L;

	private final T value;
	
	private final List<IssueTO> issues;
	
	public ResultTO(T object, List<IssueTO> issues) {
		this.value = object;
		this.issues = issues;
	}

	public T getValue() {
		return value;
	}

	public List<IssueTO> getIssues() {
		return issues;
	}
	
}
