package org.dragberry.eshop.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.dragberry.eshop.controller.exception.ResourceNotFoundException;
import org.dragberry.eshop.dal.repo.PageRepository;
import org.dragberry.eshop.service.DataImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller serves all static pages
 * @author Maksim Dragun
 */
@Controller
public class MainController {
    
    @Autowired
    @Qualifier("InSalesDataImporter")
    private DataImporter inSalesDataImporter;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    @Autowired
    private PageRepository pageRepo;
    
    /**
     * Home page
     * @return
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    @GetMapping("import/test")
    public String testImport() throws IOException {
    	inSalesDataImporter.importData(resourceLoader.getResource("classpath:data/insales_export.csv").getInputStream());
        return "home";
    }

	/**
     * Error page
     * @return
     */
    @GetMapping("${url.error}")
    public String error() {
        return "error";
    }
    
    @GetMapping("/*")
    public ModelAndView delivery(HttpServletRequest request) {
//    	return new ModelAndView("home");
        if (pageRepo.existsByReference(request.getRequestURI())) {
            return new ModelAndView(request.getRequestURI());
        }
        throw new ResourceNotFoundException();
    }
    
}
