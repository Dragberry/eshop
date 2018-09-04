package org.dragberry.eshop.service.impl;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.dragberry.eshop.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	@Qualifier("emailTemplateEngine")
	private TemplateEngine templateEngine;
	
	@Autowired
    private JavaMailSender mailSender;

	@Override
	public <T> void sendNotification(String notifier, T object, Function<T, String> notification) {
		try {
			// Prepare the evaluation context
			final Context ctx = new Context();
		    ctx.setVariable("name", "Max");
		    ctx.setVariable("subscriptionDate", new Date());
		    ctx.setVariable("hobbies", List.of("Cinema", "Sports", "Music"));
	
		    // Prepare message using a Spring helper
		    final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		    final MimeMessageHelper message =
		        new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
		    message.setSubject("Example HTML email with inline image");
		    message.setFrom("thymeleaf@example.com");
		    message.setTo(notifier);
	
		    // Create the HTML body using Thymeleaf
		    final String htmlContent = this.templateEngine.process("test-email.html", ctx);
		    message.setText(htmlContent, true); // true = isHtml
	
		    // Send mail
		    this.mailSender.send(mimeMessage);
	    } catch (MessagingException exc) {
			log.error("Notification error...", exc);
		}
	}

}
