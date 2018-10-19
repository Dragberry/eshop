package org.dragberry.eshop.service.impl;

import org.dragberry.eshop.service.AppInfoService;
import org.dragberry.eshop.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class SytemServiceImpl implements SystemService {

    private static final String SHOP = "shop";
    
    @Autowired
    private AppInfoService appInfoService;
    
    @Autowired
    @Qualifier("stringTemplateEngine")
    private TemplateEngine stringTemplateEngine;
    
    private Context context;
    
    public Context getContext() {
        if (context == null) {
            context = new Context();
            context.setVariable(SHOP, appInfoService.getShopDetails());
        }
        return context;
    }
    
    @Override
    public String processTemplate(String template) {
        return stringTemplateEngine.process(template, getContext());
    }
}
