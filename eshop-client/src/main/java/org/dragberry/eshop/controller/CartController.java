package org.dragberry.eshop.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.dragberry.eshop.model.cart.CapturedProduct;
import org.dragberry.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.log4j.Log4j;

@Log4j
@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class CartController {
    
    @Autowired
    private ProductService productService;
    
    private Map<CapturedProduct, Integer> capturedProducts = new HashMap<>();

    /**
     * Go to cart items
     * @return
     */
    @GetMapping("${url.cart}")
    public ModelAndView cartItems() {
        ModelAndView mv = new ModelAndView("pages/cart/cart-items");
        mv.addObject("capturedProducts", capturedProducts);
        return mv;
    }
    
    /**
     * Add to cart
     * @return
     */
    @PostMapping("${url.cart.add}")
    @ResponseBody
    public ResponseEntity<CapturedProduct> addToCart(@RequestBody CapturedProduct capturedProduct, HttpSession session) {
        log.info("Add to cart: " + capturedProduct);
        capturedProduct = productService.getProductCartDetails(capturedProduct);
        capturedProducts.compute(capturedProduct, (cp, quanity) -> quanity == null ? 1 : quanity + 1);
        session.setAttribute("cartProductCount", capturedProducts.values().stream().mapToInt(Integer::intValue).sum());
        return ResponseEntity.ok(capturedProduct);
    }
    
    /**
     * Cart product count
     * @return
     */
    @GetMapping("${url.cart.product-count}")
    @ResponseBody
    public ResponseEntity<Integer> cartProductCount(HttpSession session) {
        int cartProductCount = capturedProducts.values().stream().mapToInt(Integer::intValue).sum();
        session.setAttribute("cartProductCount", cartProductCount);
        return ResponseEntity.ok(cartProductCount);
    }
}
