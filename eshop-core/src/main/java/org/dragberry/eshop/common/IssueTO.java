package org.dragberry.eshop.common;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import lombok.Data;

@Data
public class IssueTO implements Serializable {

	private static final long serialVersionUID = -3093436226923145667L;

	private final String errorCode;
	
	private final List<Object> params;
	
	private final IssueType type;
	
	private final String fieldId;
	
	private String message;

	public IssueTO(String errorCode, IssueType type, Object... params) {
		this(errorCode, null, type, params);
	}
	
	public IssueTO(String errorCode, String fieldId, IssueType type, Object... params) {
		this.fieldId = fieldId;
		this.errorCode = errorCode;
		this.params = Arrays.asList(params);
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuffer sb =  new StringBuffer("Issue[").append(getType()).append("]: {errorCode: ").append(errorCode);
		if (params != null && params.size() > 0) {
			sb.append(", params: [");
			Iterator<Object> iter = params.iterator();
			while (iter.hasNext()) {
				sb.append(iter.next());
				if (iter.hasNext()) {
					sb.append(", ");
				}
			}
			sb.append("]");
		}
		if (fieldId != null) {
			sb.append(", fieldId: ").append(fieldId);
		}
		return sb.append("}").toString();
	}

}
