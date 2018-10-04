package org.dragberry.eshop.service.impl;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Supplier;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.dragberry.eshop.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class NotificationServiceImpl implements NotificationService {
    
    private static final HashSet<String> TEMPLATE_SELECTORS_SUBJECT = new HashSet<>(Arrays.asList("message-subject"));
    
    private static final HashSet<String> TEMPLATE_SELECTORS_BODY = new HashSet<>(Arrays.asList("message-body"));

	@Autowired
	@Qualifier("emailTemplateEngine")
	private TemplateEngine templateEngine;
	
	@Autowired
    private JavaMailSender mailSender;
	
	private class EmailHelper {
	    
        void send(String recipient, String template, Map<String, Object> objects) {
	        Context ctx = new Context();
	        objects.forEach(ctx::setVariable);
            send(recipient,
                    () -> templateEngine.process(template, TEMPLATE_SELECTORS_BODY, ctx),
                    () -> templateEngine.process(template, TEMPLATE_SELECTORS_SUBJECT, ctx));
        }
	    
	    <T> void send(String recipient, Supplier<String> bodyProvider, Supplier<String> headerProvider) {
	        try {
    	        final MimeMessage mimeMessage = mailSender.createMimeMessage();
                final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
                message.setTo(recipient);
                message.setSubject(headerProvider.get());
                message.setText(bodyProvider.get(), true);
                mailSender.send(mimeMessage);
	        } catch (MessagingException exc) {
	            log.error(MessageFormat.format("An error has occurred during sending a email: [recipient={0}]", exc));
	        }
	    }
	}

	@Override
	@Async("notifications")
	public void sendNotification(String recipient, String template, Map<String, Object> objects) {
	    new EmailHelper().send(recipient, template, objects);
	}
}

