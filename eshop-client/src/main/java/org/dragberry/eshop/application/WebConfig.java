package org.dragberry.eshop.application;

import org.dragberry.eshop.filter.AuditLogFilter;
import org.dragberry.eshop.interceptor.AppInfoInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { AppInfoInterceptor.class })
public class WebConfig {

    @Bean
    public AppInfoInterceptor appInfoInterceptor() {
        return new AppInfoInterceptor();
    }
    
    @Bean
    public AuditLogFilter auditLogInterceptor() {
        return new AuditLogFilter();
    }
    
    @Bean
    public FilterRegistrationBean<AuditLogFilter> loggingFilter(){
        FilterRegistrationBean<AuditLogFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(auditLogInterceptor());
        return registrationBean;    
    }
    
}
