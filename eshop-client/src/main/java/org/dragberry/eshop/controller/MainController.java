package org.dragberry.eshop.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.dragberry.eshop.controller.exception.ResourceNotFoundException;
import org.dragberry.eshop.dal.entity.Page;
import org.dragberry.eshop.dal.repo.PageRepository;
import org.dragberry.eshop.navigation.Breadcrumb;
import org.dragberry.eshop.service.CommentService;
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
	private CommentService commentService;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    @Autowired
    private PageRepository pageRepo;
    
	/**
     * Error page
     * @return
     */
    @GetMapping("${url.error}")
    public String error() {
        return "error";
    }
    
    /**
     * Handle all request which should have a page in DB
     * @param request
     * @return
     */
    @GetMapping("/*")
    public ModelAndView handlePage(HttpServletRequest request) {
//    	return new ModelAndView("pages/home");
    	Optional<Page> page = pageRepo.findByReference(request.getRequestURI());
        if (page.isPresent()) {
        	ModelAndView mv = new ModelAndView(page.get().getViewName());
        	if (page.get().getBreadcrumbTitle() != null) {
        		mv.addObject(Breadcrumb.MODEL_BREADCRUMB, Breadcrumb.builder().append(page.get().getTitle(), StringUtils.EMPTY));
        	}
            return mv;
        }
        throw new ResourceNotFoundException();
    }
    
    /**
     * Handle comments page
     * @return
     */
    @GetMapping("${url.comments}")
    public ModelAndView comments(HttpServletRequest request) {
        ModelAndView mv = handlePage(request);
        mv.addObject("comments", commentService.getCommentList());
        mv.setViewName("pages/otzyvy-pokupatelei");
        return mv;
    }
    
    /**
     * Handle import request for debug purposes
     * @return
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    @GetMapping("import/test")
    public String testImport() throws IOException {
        inSalesDataImporter.importData(resourceLoader.getResource("classpath:data/insales_export.csv").getInputStream());
        return "pages/home";
    }
}
