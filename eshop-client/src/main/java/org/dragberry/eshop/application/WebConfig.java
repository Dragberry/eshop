package org.dragberry.eshop.application;

import org.dragberry.eshop.filter.AuditLogFilter;
import org.dragberry.eshop.interceptor.AppInfoInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { AppInfoInterceptor.class, AuditLogFilter.class })
public class WebConfig {

}
