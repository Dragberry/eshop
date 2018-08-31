package org.dragberry.eshop.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.dragberry.eshop.controller.exception.BadRequestException;
import org.dragberry.eshop.model.cart.CapturedProduct;
import org.dragberry.eshop.model.cart.CapturedProductState;
import org.dragberry.eshop.model.cart.CartState;
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
    
    private static enum State {
        CART_ITEMS("pages/cart/cart-items"), ORDERING("pages/cart/ordering");
        
        public final String template;
        
        private State(String template) {
            this.template = template;
        }
    }
    
    @Autowired
    private ProductService productService;
    
    private Map<CapturedProduct, CapturedProductState> capturedProducts = new HashMap<>();
    
    private State state = State.ORDERING;

    /**
     * Go to cart items
     * @return
     */
    @GetMapping("${url.cart}")
    public ModelAndView cartItems(HttpSession session) {
        ModelAndView mv = new ModelAndView(state.template);
        mv.addObject("capturedProducts", capturedProducts);
        updateCartState(session);
        return mv;
    }
    
    /**
     * Updates product count and sum in session. Wraps result in holder
     * @param session
     * @param change
     */
    private <T> CartState<T> updateCartState(HttpSession session, T change) {
        var cartState = new CartState<T>();
        int quantity = capturedProducts.values().stream()
                .mapToInt(CapturedProductState::getQuantity).sum();
        cartState.setQuantity(quantity);
        session.setAttribute("cartProductCount", quantity);
        BigDecimal sum = capturedProducts.values().stream()
                .map(CapturedProductState::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        cartState.setSum(sum);
        session.setAttribute("cartProductSum", sum);
        cartState.setChange(change);
        return cartState;
    }
    
    /**
     * Updates product count and sum in session
     * @param session
     * @param change
     */
    private void updateCartState(HttpSession session) {
        updateCartState(session, null);
    }
    
    /**
     * Decrement product item
     * @return
     */
    @PostMapping("${url.cart.decrement}")
    @ResponseBody
    public ResponseEntity<CartState<CapturedProductState>> decrement(@RequestBody CapturedProduct capturedProduct, HttpSession session) {
        if (capturedProducts.containsKey(capturedProduct)) {
            CapturedProductState state = capturedProducts.get(capturedProduct);
            state.decrement();
            return ResponseEntity.ok(updateCartState(session, state));
        }
        throw new BadRequestException();
    }
    
    /**
     * Increment product item
     * @return
     */
    @PostMapping("${url.cart.increment}")
    @ResponseBody
    public ResponseEntity<CartState<CapturedProductState>> increment(@RequestBody CapturedProduct capturedProduct, HttpSession session) {
        if (capturedProducts.containsKey(capturedProduct)) {
            CapturedProductState state = capturedProducts.get(capturedProduct);
            state.increment();
            return ResponseEntity.ok(updateCartState(session, state));
        }
        throw new BadRequestException();
    }
    
    /**
     * Remove from cart
     * @return
     */
    @PostMapping("${url.cart.remove}")
    @ResponseBody
    public ResponseEntity<CartState<CapturedProduct>> removeFromCart(@RequestBody CapturedProduct capturedProduct, HttpSession session) {
        if (capturedProducts.containsKey(capturedProduct)) {
            capturedProducts.remove(capturedProduct);
            return ResponseEntity.ok(updateCartState(session, capturedProduct));
        }
        throw new BadRequestException();
    }
    
    /**
     * Add to cart
     * @return
     */
    @PostMapping("${url.cart.add}")
    @ResponseBody
    public ResponseEntity<CartState<CapturedProduct>> addToCart(@RequestBody CapturedProduct capturedProduct, HttpSession session) {
        capturedProduct = productService.getProductCartDetails(capturedProduct);
        if (capturedProduct != null) {
            capturedProducts.computeIfAbsent(capturedProduct, cp -> new CapturedProductState(cp.getProductId(), cp.getPrice())).increment();
            return ResponseEntity.ok(updateCartState(session, capturedProduct));
        } throw new BadRequestException();
    }
    
}
