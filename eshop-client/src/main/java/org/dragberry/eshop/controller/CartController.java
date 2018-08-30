package org.dragberry.eshop.controller;

import java.util.HashMap;
import java.util.Map;

import org.dragberry.eshop.model.cart.CapturedProduct;
import org.dragberry.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.log4j.Log4j;

@Log4j
@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class CartController {
    
    @Autowired
    private ProductService productService;
    
    private Map<CapturedProduct, Integer> capturedProducts = new HashMap<>();

    /**
     * Add to cart
     * @return
     */
    @PostMapping("${url.cart.add}")
    @ResponseBody
    public ResponseEntity<CapturedProduct> addToCart(@RequestBody CapturedProduct capturedProduct) {
        log.info("Add to cart: " + capturedProduct);
        capturedProduct = productService.getProductCartDetails(capturedProduct);
        capturedProducts.compute(capturedProduct, (cp, quanity) -> quanity == null ? 1 : quanity + 1);
        return ResponseEntity.ok(capturedProduct);
    }
}
