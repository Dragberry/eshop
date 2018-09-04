package org.dragberry.eshop.service.notification;

public abstract class Notification<T> {

	private final T object;
	
	public Notification(T object) {
		this.object = object;
	}
	
	public abstract String build();
}
