package org.dragberry.eshop.files.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.dragberry.eshop.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ImagesController {
	
	@Autowired
	private ImageService imageService;

	/**
     * Get an image
     * @return
     * @throws IOException 
     */
    @GetMapping({"${url.images}/{folder}/{imageName:.+}"})
    public void getImage(HttpServletResponse response,
            @PathVariable String folder,
            @PathVariable String imageName) throws IOException {
        try (InputStream is = imageService.getImage(folder, imageName)) {
            IOUtils.copy(is, response.getOutputStream());
        }
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
