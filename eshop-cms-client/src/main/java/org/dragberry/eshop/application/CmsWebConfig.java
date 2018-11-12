package org.dragberry.eshop.application;

import org.dragberry.eshop.cms.controller.Controllers;
import org.dragberry.eshop.cms.security.Security;
import org.dragberry.eshop.cms.service.impl.Services;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {  Services.class, Controllers.class, Security.class })
public class CmsWebConfig {

}
