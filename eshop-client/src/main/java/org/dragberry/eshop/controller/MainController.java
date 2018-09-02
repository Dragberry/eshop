package org.dragberry.eshop.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.dragberry.eshop.service.DataImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This controller serves all static pages
 * @author Maksim Dragun
 */
@Controller
public class MainController {
    
    @Autowired
    private DataImporter dataImporter;
    
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
        dataImporter.importData(resourceLoader.getResource("classpath:data/test-price.xls").getInputStream());
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
     * Contacts page
     * @return
     */
    @GetMapping("${url.contacts}")
    public String contacts() {
        return "pages/contacts";
    }
    
    /**
     * Delivery page
     * @return
     */
    @GetMapping("${url.delivery}")
    public String delivery() {
        return "pages/delivery";
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
