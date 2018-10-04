package org.dragberry.eshop.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.dragberry.eshop.controller.exception.ResourceNotFoundException;
import org.dragberry.eshop.dal.entity.Page;
import org.dragberry.eshop.dal.repo.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {

    @Autowired
    private PageRepository pageRepo;
    
    @GetMapping("/*")
    public ModelAndView delivery(HttpServletRequest request) {
        Optional<Page> page = pageRepo.findByReference(request.getRequestURI());
        if (page.isPresent()) {
            ModelAndView mv = new ModelAndView("pages/custom-page");
            return mv;
        }
        throw new ResourceNotFoundException();
    }
}
