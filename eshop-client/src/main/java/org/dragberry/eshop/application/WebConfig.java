package org.dragberry.eshop.application;

import org.dragberry.eshop.filter.RequestLogFilter;
import org.dragberry.eshop.interceptor.AppInfoInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { AppInfoInterceptor.class, RequestLogFilter.class })
public class WebConfig {

}
