package org.dragberry.era.domain;

import java.text.MessageFormat;

public interface BaseEnum<E> {

	String UNKNOWN_VALUE_MSG = "Unknown {0} value: {1}!";
	String NPE_MSG = "{0} cannot be null!";
	
	static RuntimeException unknownValueException(Class<?> clazz, Object value) {
		return new RuntimeException(MessageFormat.format(UNKNOWN_VALUE_MSG, clazz.getName(), value));
	}
	
	static RuntimeException npeException(Class<?> clazz) {
		return new NullPointerException(MessageFormat.format(NPE_MSG, clazz.getName()));
	}
	
	E getValue();
	
}
