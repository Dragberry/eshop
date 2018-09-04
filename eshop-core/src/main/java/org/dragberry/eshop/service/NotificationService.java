package org.dragberry.eshop.service;

import java.util.function.Function;

public interface NotificationService {
	
	<T> void sendNotification(String notifier, T object, Function<T, String> notification);

}
