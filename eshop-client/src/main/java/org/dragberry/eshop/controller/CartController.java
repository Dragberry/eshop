package org.dragberry.eshop.controller;

import java.math.BigDecimal;
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
     * Remove from cart
     * @return
     */
    @PostMapping("${url.cart.remove}")
    @ResponseBody
    public ResponseEntity<CapturedProduct> removeFromCart(@RequestBody CapturedProduct capturedProduct, HttpSession session) {
        log.info("Remove from cart: " + capturedProduct);
        capturedProducts.remove(capturedProduct);
        session.setAttribute("cartProductCount", capturedProducts.values().stream().mapToInt(Integer::intValue).sum());
        session.setAttribute("cartProductSum", capturedProducts.entrySet().stream().map(entry -> {
        	return entry.getKey().getPrice().multiply(new BigDecimal(entry.getValue()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add));
        return ResponseEntity.ok(capturedProduct);
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
        session.setAttribute("cartProductSum", capturedProducts.entrySet().stream().map(entry -> {
        	return entry.getKey().getPrice().multiply(new BigDecimal(entry.getValue()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add));
        return ResponseEntity.ok(capturedProduct);
    }
    
    /**
     * Get cart product count
     * @return
     */
    @GetMapping("${url.cart.count}")
    public String productCount() {
        return "components/cart-components :: cartProductCount";
    }
    
    /**
     * Get cart product sum
     * @return
     */
    @GetMapping("${url.cart.sum}")
    public String productSum() {
        return "components/cart-components :: cartProductSum";
    }
}
