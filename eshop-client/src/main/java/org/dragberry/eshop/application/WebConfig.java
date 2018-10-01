package org.dragberry.eshop.application;

import org.dragberry.eshop.interceptor.AppInfoInterceptor;
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
    
}
