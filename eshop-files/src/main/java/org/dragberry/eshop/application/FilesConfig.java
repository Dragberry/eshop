package org.dragberry.eshop.application;

import org.dragberry.eshop.files.controller.ImagesController;
import org.dragberry.eshop.files.service.impl.ImageServiceImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { ImagesController.class, ImageServiceImpl.class })
public class FilesConfig {

}
