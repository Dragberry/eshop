package org.dragberry.eshop.application;

import org.dragberry.eshop.interceptor.AppInfoInterceptor;
import org.dragberry.eshop.interceptor.AuditLogInterceptor;
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
    public AuditLogInterceptor auditLogInterceptor() {
        return new AuditLogInterceptor();
    }
    
}
