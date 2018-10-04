package org.dragberry.eshop.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.dragberry.eshop.controller.exception.ResourceNotFoundException;
import org.dragberry.eshop.dal.entity.Page;
import org.dragberry.eshop.dal.repo.PageRepository;
import org.dragberry.eshop.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {

	@Autowired
	private SystemService systemService;
	
    @Autowired
    private PageRepository pageRepo;
    
    @GetMapping("/*")
    public ModelAndView delivery(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView(request.getRequestURI());
        return mv;
    }
}
