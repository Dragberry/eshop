package org.dragberry.eshop.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.dragberry.eshop.controller.exception.BadRequestException;
import org.dragberry.eshop.model.cart.CapturedProduct;
import org.dragberry.eshop.model.cart.CapturedProductState;
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

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class CartController {
    
    @Autowired
    private ProductService productService;
    
    private Map<CapturedProduct, CapturedProductState> capturedProducts = new HashMap<>();

    /**
     * Go to cart items
     * @return
     */
    @GetMapping("${url.cart}")
    public ModelAndView cartItems(HttpSession session) {
        ModelAndView mv = new ModelAndView("pages/cart/cart-items");
        mv.addObject("capturedProducts", capturedProducts);
        updateCartState(session);
        return mv;
    }
    
    /**
     * Updates product count and sum in session
     * @param session
     */
    private void updateCartState(HttpSession session) {
        session.setAttribute("cartProductCount", capturedProducts.values().stream()
                .mapToInt(CapturedProductState::getQuantity).sum());
        session.setAttribute("cartProductSum", capturedProducts.values().stream()
                .map(CapturedProductState::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
    }
    
    /**
     * Decrement product item
     * @return
     */
    @PostMapping("${url.cart.decrement}")
    @ResponseBody
    public ResponseEntity<CapturedProductState> decrement(@RequestBody CapturedProduct capturedProduct, HttpSession session) {
        if (capturedProducts.containsKey(capturedProduct)) {
            CapturedProductState state = capturedProducts.get(capturedProduct);
            state.decrement();
            updateCartState(session);
            return ResponseEntity.ok(state);
        }
        throw new BadRequestException();
    }
    
    /**
     * Increment product item
     * @return
     */
    @PostMapping("${url.cart.increment}")
    @ResponseBody
    public ResponseEntity<CapturedProductState> increment(@RequestBody CapturedProduct capturedProduct, HttpSession session) {
        if (capturedProducts.containsKey(capturedProduct)) {
            CapturedProductState state = capturedProducts.get(capturedProduct);
            state.increment();
            updateCartState(session);
            return ResponseEntity.ok(state);
        }
        throw new BadRequestException();
    }
    
    /**
     * Remove from cart
     * @return
     */
    @PostMapping("${url.cart.remove}")
    @ResponseBody
    public ResponseEntity<CapturedProduct> removeFromCart(@RequestBody CapturedProduct capturedProduct, HttpSession session) {
        if (capturedProducts.containsKey(capturedProduct)) {
            capturedProducts.remove(capturedProduct);
            updateCartState(session);
            return ResponseEntity.ok(capturedProduct);
        }
        throw new BadRequestException();
    }
    
    /**
     * Add to cart
     * @return
     */
    @PostMapping("${url.cart.add}")
    @ResponseBody
    public ResponseEntity<CapturedProduct> addToCart(@RequestBody CapturedProduct capturedProduct, HttpSession session) {
        capturedProduct = productService.getProductCartDetails(capturedProduct);
        if (capturedProduct != null) {
            capturedProducts.computeIfAbsent(capturedProduct, cp -> new CapturedProductState(cp.getProductId(), cp.getPrice())).increment();
            updateCartState(session);
            return ResponseEntity.ok(capturedProduct);
        } throw new BadRequestException();
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
