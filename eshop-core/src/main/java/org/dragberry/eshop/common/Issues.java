package org.dragberry.eshop.common;

public final class Issues {
	
	private Issues() {}
	
	public static IssueTO error(String errorCode, Object... params) {
		return new IssueTO(errorCode, IssueType.ERROR, params);
	}
	
	public static IssueTO warning(String errorCode, Object... params) {
		return new IssueTO(errorCode, IssueType.WARNING, params);
	}
	
	public static IssueTO error(String errorCode, String fieldId, Object... params) {
		return new IssueTO(errorCode, fieldId, IssueType.ERROR, params);
	}
	
	public static IssueTO warning(String errorCode, String fieldId, Object... params) {
		return new IssueTO(errorCode, fieldId, IssueType.WARNING, params);
	}


}
