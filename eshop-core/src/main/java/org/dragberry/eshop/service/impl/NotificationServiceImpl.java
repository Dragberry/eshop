package org.dragberry.eshop.service.impl;

import java.util.function.Function;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.dragberry.eshop.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
    private JavaMailSender sender;

	@Override
	public <T> void sendNotification(String notifier, T object, Function<T, String> notification) {
        MimeMessage message = sender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	    try {
	    	helper.setTo(notifier);
	    	helper.setText("How are you?");
	    	helper.setSubject(notification.apply(object));
	    } catch (MessagingException exc) {
			log.error(message, exc);
		}
	    sender.send(message);
	}

}
