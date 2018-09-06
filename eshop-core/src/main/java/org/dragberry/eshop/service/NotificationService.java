package org.dragberry.eshop.service;

import java.util.Map;

public interface NotificationService {
	
	void sendNotification(String recipient, String template, Map<String, Object> objects);

}
