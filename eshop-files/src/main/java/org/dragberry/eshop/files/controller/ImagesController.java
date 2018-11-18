package org.dragberry.eshop.files.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.dragberry.eshop.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class ImagesController {
	
	@Autowired
	private ImageService imageService;

	/**
     * Get an image
     * @return
     * @throws IOException 
     */
    @GetMapping("${url.files.images}/*")
    public void getImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        imageService.findImage(request.getRequestURI()).ifPresent(img -> {
        	response.setContentType(img.getContentType());
        	try (InputStream is = imageService.getImage(img.getPath())) {
                IOUtils.copy(is, response.getOutputStream());
            } catch (IOException exc) {
				log.error("An error has occurred while getting an image", exc);
			}
        });
    	
    }
	
	/**
     * Get an product image
     * @return
	 * @throws IOException 
     */
    @GetMapping({"${url.images}/{productKey}/{productArticle}/{imageName:.+}"})
    public void getProductImage(HttpServletResponse response,
            @PathVariable Long productKey,
            @PathVariable String productArticle,
            @PathVariable String imageName) throws IOException {
        try (InputStream is = imageService.getProductImage(productKey, productArticle, imageName)) {
            IOUtils.copy(is, response.getOutputStream());
        }
    }
	
}
