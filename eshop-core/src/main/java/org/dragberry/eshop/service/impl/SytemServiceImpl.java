package org.dragberry.eshop.service.impl;

import java.util.Arrays;
import java.util.List;

import org.dragberry.eshop.model.common.Phone;
import org.dragberry.eshop.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class SytemServiceImpl implements SystemService {

    @Value("${application.title}")
    private String shopName;
    
    @Autowired
    @Qualifier("templateEngine")
    private TemplateEngine stringTemplateEngine;
    
    private Context context;
    
    public List<Phone> getPhones() {
        return Arrays.asList(new Phone(Phone.MTS, "8033 375-90-80"), new Phone(Phone.VELCOM, "8029 375-90-80"));
    }
    
    public Context getContext() {
        if (context == null) {
            context = new Context();
            context.setVariable("shopName", shopName);
            context.setVariable("phones", getPhones());
        }
        return context;
    }
    
    @Override
    public String processTemplate(String template) {
        return stringTemplateEngine.process(template, getContext());
    }
}
