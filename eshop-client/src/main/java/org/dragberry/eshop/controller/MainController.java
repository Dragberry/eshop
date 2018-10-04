package org.dragberry.eshop.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.dragberry.eshop.service.DataImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.log4j.Log4j;

/**
 * This controller serves all static pages
 * @author Maksim Dragun
 */
@Controller
@Log4j
public class MainController {
    
    @Autowired
    @Qualifier("InSalesDataImporter")
    private DataImporter inSalesDataImporter;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
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
     * Home page
     * @return
     */
	@GetMapping("${url.home}")
	public String home() {
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
    
    /**
     * Account page
     * @return
     */
    @GetMapping("${url.account}")
    public String account() {
        return "pages/account";
    }
	
   /**
    * Payment and installment page
    * @return
    */
   @GetMapping("${url.payment-and-installment}")
   public String payment() {
       return "pages/payment-and-installment";
   }
    
    /**
     * Reviews page
     * @return
     */
    @GetMapping("${url.reviews}")
    public String reviews() {
        return "pages/reviews";
    }
    
    /**
     * Wholesale trade page
     * @return
     */
    @GetMapping("${url.wholesale-trade}")
    public String wholesaleTrade() {
        return "pages/wholesale-trade";
    }
}
