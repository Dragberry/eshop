package org.dragberry.eshop.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import org.dragberry.eshop.dal.entity.Page;
import org.dragberry.eshop.dal.repo.PageRepository;
import org.dragberry.eshop.filter.RequestLogFilter;
import org.dragberry.eshop.interceptor.AppInfoInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.cache.AlwaysValidCacheEntryValidity;
import org.thymeleaf.cache.ICacheEntryValidity;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresolver.AbstractTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;

@Configuration
@ComponentScan(basePackageClasses = { AppInfoInterceptor.class, RequestLogFilter.class })
public class WebConfig {
    
    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private PageRepository pageRepo;

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
        thymeleafViewResolver.setTemplateEngine(templateEngine());
        thymeleafViewResolver.setOrder(1);
        return thymeleafViewResolver;
    }
    /**
     * Template engine to process String templates
     * @return
     */
    @Bean
    public SpringTemplateEngine templateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(pageTemplateResolver());
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        templateEngine.addTemplateResolver(stringTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(messageSource);
        return templateEngine;
    }
    
    private ITemplateResolver pageTemplateResolver() {
    	PageTemplateResolver templateResolver = new PageTemplateResolver();
    	templateResolver.setOrder(1);
    	templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
    	return templateResolver;
    }
    
    private ITemplateResolver htmlTemplateResolver() {
        final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setOrder(2);
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templateResolver.setCacheable(false);
        return templateResolver;
    }
    
    private ITemplateResolver stringTemplateResolver() {
        final StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setOrder(3);
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCacheable(false);
        return templateResolver;
    }
    
    private class PageTemplateResolver extends AbstractConfigurableTemplateResolver {

    	@Override
    	protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate,
    			String template, String resourceName, String characterEncoding,
    			Map<String, Object> templateResolutionAttributes) {
	    		return new ITemplateResource() {
					
					private Optional<Page> page = pageRepo.findByReference(template);
					
					@Override
					public ITemplateResource relative(String relativeLocation) {
						return null;
					}
					
					@Override
					public Reader reader() throws IOException {
						return new StringReader(page.get().getContent());
					}
					
					@Override
					public String getDescription() {
						return page.get().getName();
					}
					
					@Override
					public String getBaseName() {
						return page.get().getName();
					}
					
					@Override
					public boolean exists() {
						return page.isPresent();
					}
				};
    	}
    	
		@Override
		protected TemplateMode computeTemplateMode(IEngineConfiguration configuration, String ownerTemplate,
				String template, Map<String, Object> templateResolutionAttributes) {
			return TemplateMode.HTML;
		}

		@Override
		protected ICacheEntryValidity computeValidity(IEngineConfiguration configuration, String ownerTemplate,
				String template, Map<String, Object> templateResolutionAttributes) {
			return new AlwaysValidCacheEntryValidity();
		}
    	
    }
}
