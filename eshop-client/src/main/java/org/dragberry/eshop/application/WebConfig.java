package org.dragberry.eshop.application;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dragberry.eshop.dal.entity.Page;
import org.dragberry.eshop.dal.repo.PageRepository;
import org.dragberry.eshop.dal.repo.RequestLogRepository;
import org.dragberry.eshop.filter.RequestLogFilter;
import org.dragberry.eshop.interceptor.AppInfoInterceptor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.mobile.device.DeviceResolverRequestFilter;
import org.springframework.mobile.device.view.LiteDeviceDelegatingViewResolver;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.cache.AlwaysValidCacheEntryValidity;
import org.thymeleaf.cache.ICacheEntryValidity;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;

@Configuration
@ComponentScan(basePackageClasses = { AppInfoInterceptor.class, RequestLogFilter.class })
public class WebConfig {
    
	private final static String MOBILE_PREFIX = "mobile/";
	
	private final static String NORMAL_PREFIX = "normal/";
	
	private final static String TABLET_PREFIX = "mobile/";
	
	private final static Pattern DB_PAGE_PATTERN = Pattern.compile("(.*?/)(db/.*?)");
	
    private Collection<IDialect> dialects;
    
    @Autowired
    private WebApplicationContext applicationContext;
    
    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private PageRepository pageRepo;
    
    @Autowired
    private RequestLogRepository requestLogRepository;
    
    public WebConfig(ObjectProvider<Collection<IDialect>> dialectsProvider) {
        this.dialects = dialectsProvider.getIfAvailable(Collections::emptyList);
    }
    
    @Bean
    public FilterRegistrationBean<DeviceResolverRequestFilter> deviceResolverFilter(){
        FilterRegistrationBean<DeviceResolverRequestFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new DeviceResolverRequestFilter());
        bean.setOrder(Ordered.LOWEST_PRECEDENCE + 1);
        return bean;    
    }
    
    @Bean
    public FilterRegistrationBean<RequestLogFilter> requestLogFilter(){
        FilterRegistrationBean<RequestLogFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new RequestLogFilter(requestLogRepository));
        bean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return bean;    
    }
    
    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setCache(false);
        resolver.setOrder(2);
        return resolver;
    }
    
    @Bean
    public LiteDeviceDelegatingViewResolver liteDeviceAwareViewResolver(@Qualifier("thymeleafViewResolver") ViewResolver delegate) {
        LiteDeviceDelegatingViewResolver resolver = new LiteDeviceDelegatingViewResolver(delegate);
        resolver.setMobilePrefix(MOBILE_PREFIX);
        resolver.setNormalPrefix(NORMAL_PREFIX);
        resolver.setTabletPrefix(TABLET_PREFIX);
        resolver.setOrder(1);
        return resolver;
    }
    
    /**
     * Template engine to process String templates
     * @return
     */
    @Bean
    public SpringTemplateEngine templateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(dbPageTemplateResolver());
        templateEngine.addTemplateResolver(springTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(messageSource);
        this.dialects.forEach(templateEngine::addDialect);
        return templateEngine;
    }
    
    /**
     * The template resolver for DB pages
     * @return
     */
    private ITemplateResolver dbPageTemplateResolver() {
    	DBPageTemplateResolver templateResolver = new DBPageTemplateResolver();
    	templateResolver.setOrder(1);
    	templateResolver.setCheckExistence(true);
    	templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
    	return templateResolver;
    }
    
    /**
     * This is a default spring template resolver
     * @return
     */
    private ITemplateResolver springTemplateResolver() {
        return enrichSpringTemplateResolver(new SpringResourceTemplateResolver(), 2);
    }
    
    /**
     * Enriches spring template resolver
     * @param templateResolver
     * @param order
     * @return
     */
    private SpringResourceTemplateResolver enrichSpringTemplateResolver(SpringResourceTemplateResolver templateResolver, int order) {
    	 templateResolver.setApplicationContext(applicationContext);
         templateResolver.setPrefix("classpath:templates/");
         templateResolver.setSuffix(".html");
         templateResolver.setTemplateMode(TemplateMode.HTML);
         templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
         templateResolver.setCheckExistence(true);
         templateResolver.setCacheable(false);
         templateResolver.setOrder(order);
         return templateResolver;
    }
    
    /**
     * This class is used to resolve template from database, see {@link Page}
     * @author Drahun Maksim
     */
    private class DBPageTemplateResolver extends AbstractConfigurableTemplateResolver {

    	@Override
    	protected String computeResourceName(IEngineConfiguration configuration, String ownerTemplate, String template,
    			String prefix, String suffix, boolean forceSuffix, Map<String, String> templateAliases,
    			Map<String, Object> templateResolutionAttributes) {
    		// TODO Auto-generated method stub
    		return super.computeResourceName(configuration, ownerTemplate, template, prefix, suffix, forceSuffix, templateAliases,
    				templateResolutionAttributes);
    	}
    	
    	@Override
    	protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate,
    			String template, String resourceName, String characterEncoding,
    			Map<String, Object> templateResolutionAttributes) {
	    		return new DBPageTemplateResource(resourceName);
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
    
    /**
     * Template resource for pages which are stored in database
     * @author Drahun Maksim
     *
     */
    private class DBPageTemplateResource implements ITemplateResource {
        
        private final String template;
        
        private Page page;
        
        private boolean mobile;

        public DBPageTemplateResource(String template) {
            this.template = template;
        }
        
        @Override
        public ITemplateResource relative(String relativeLocation) {
            return new DBPageTemplateResource(relativeLocation);
        }
        
        @Override
        public Reader reader() throws IOException {
            return new StringReader(mobile ? page.getContentMobile() : page.getContent());
        }
        
        @Override
        public String getDescription() {
            return page.getTitle();
        }
        
        @Override
        public String getBaseName() {
            return page.getTitle();
        }
        
        @Override
        public boolean exists() {
        	Matcher matcher = DB_PAGE_PATTERN.matcher(template);
            if (matcher.matches()) {
                Optional<Page> result = pageRepo.findByViewName(matcher.group(2));
                if (result.isPresent()) {
                    this.page = result.get();
                    this.mobile = MOBILE_PREFIX.equals(matcher.group(1));
                    return true;
                }
            }
            return false;
        }
    }
}
