package org.dragberry.eshop.application;

import org.dragberry.eshop.filter.RequestLogFilter;
import org.dragberry.eshop.interceptor.AppInfoInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Configuration
@ComponentScan(basePackageClasses = { AppInfoInterceptor.class, RequestLogFilter.class })
public class WebConfig {
    
    @Autowired
    private MessageSource messageSource;

    /**
     * TEmpalte engine to process String templates
     * @return
     */
    @Bean("stringTemplateEngine")
    public TemplateEngine stringTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(stringTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(messageSource);
        return templateEngine;
    }
    
    private ITemplateResolver stringTemplateResolver() {
        final StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCacheable(false);
        return templateResolver;
    }
}
