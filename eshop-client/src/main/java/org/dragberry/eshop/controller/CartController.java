package org.dragberry.eshop.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.dragberry.eshop.controller.exception.BadRequestException;
import org.dragberry.eshop.model.cart.CapturedProduct;
import org.dragberry.eshop.model.cart.CapturedProductState;
import org.dragberry.eshop.model.cart.CartState;
import org.dragberry.eshop.model.cart.OrderDetails;
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
    
    public static enum CartStep {
        EDITING("pages/cart/editing-cart-items"),
        ORDERING("pages/cart/ordering"),
        SUCCESS("pages/cart/success");
        
        public final String template;
        
        private CartStep(String template) {
            this.template = template + " :: cart-content";
        }
        
        public String getTemplate() {
        	return template;
        }
    }
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private HttpSession session;
    
    @Autowired
    private HttpServletRequest request;
    
    private final Map<CapturedProduct, CapturedProductState> capturedProducts = new HashMap<>();
    
    private final OrderDetails orderDetails = new OrderDetails();
    
    private CartStep cartStep = CartStep.EDITING;

    /**
     * Go to cart items
     * @return
     */
    @GetMapping("${url.cart}")
    public ModelAndView cartItems() {
    	if (cartStep == null || cartStep == CartStep.SUCCESS) {
    		cartStep = CartStep.EDITING;
    	}
        ModelAndView mv = new ModelAndView("pages/cart/cart");
        mv.addObject("orderDetails", orderDetails);
        mv.addObject("cartStep", cartStep);
        mv.addObject("capturedProducts", capturedProducts);
        updateCartState();
        return mv;
    }
    
    /**
     * Go to ordering page
     * @return
     */
    @PostMapping("${url.cart.submit-order}")
    public ModelAndView submitOrder() {
    	List<String> validationErrors = new ArrayList<>();
    	String phone = request.getParameter("phone");
    	if (StringUtils.isNotBlank(phone)) {
    		orderDetails.setPhone(phone);
    	} else {
    		validationErrors.add("Phone is empty");
    	}
    	saveOrderDetails();
    	
    	if (validationErrors.isEmpty()) {
    		capturedProducts.clear();
    		cartStep = CartStep.SUCCESS;
    	}
    	ModelAndView mv = new ModelAndView(cartStep.template);
		mv.addObject("orderDetails", orderDetails);
        mv.addObject("capturedProducts", capturedProducts);
        mv.addObject("validationErrors", validationErrors);
        updateCartState();
        return mv;
    }
    
    /**
     * Save order details from the request
     */
    public void saveOrderDetails() {
    	orderDetails.setPhone(request.getParameter("phone"));
    	orderDetails.setFullName(request.getParameter("fullName"));
    	orderDetails.setAddress(request.getParameter("address"));
    	orderDetails.setEmail(request.getParameter("email"));
    	orderDetails.setComment(request.getParameter("comment"));
    	orderDetails.setDeliveryType(request.getParameter("deliveryType"));
    	orderDetails.setPaymentMethod(request.getParameter("paymentMethod"));
    }
    
    /**
     * Go to next step
     * @return
     */
    @GetMapping("${url.cart.next}")
    public ModelAndView nextStep() {
    	if (updateCartState().getQuantity() > 0) {
    		cartStep = CartStep.ORDERING;
    	}
        ModelAndView mv = new ModelAndView(cartStep.template);
        mv.addObject("orderDetails", orderDetails);
        mv.addObject("capturedProducts", capturedProducts);
        updateCartState();
        return mv;
    }
    
    /**
     * Go to previous step
     * @return
     */
    @GetMapping("${url.cart.back}")
    public ModelAndView backStep() {
		cartStep = CartStep.EDITING;
        ModelAndView mv = new ModelAndView(cartStep.template);
        mv.addObject("capturedProducts", capturedProducts);
        updateCartState();
        return mv;
    }
    
    /**
     * Updates product count and sum in session. Wraps result in holder
     * @param change
     */
    private <T> CartState<T> updateCartState(T change) {
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
     */
    private CartState<?> updateCartState() {
        return updateCartState(null);
    }
    
    /**
     * Decrement product item
     * @return
     */
    @PostMapping("${url.cart.decrement}")
    @ResponseBody
    public ResponseEntity<CartState<CapturedProductState>> decrement(@RequestBody CapturedProduct capturedProduct) {
        if (capturedProducts.containsKey(capturedProduct)) {
            CapturedProductState state = capturedProducts.get(capturedProduct);
            state.decrement();
            return ResponseEntity.ok(updateCartState(state));
        }
        throw new BadRequestException();
    }
    
    /**
     * Increment product item
     * @return
     */
    @PostMapping("${url.cart.increment}")
    @ResponseBody
    public ResponseEntity<CartState<CapturedProductState>> increment(@RequestBody CapturedProduct capturedProduct) {
        if (capturedProducts.containsKey(capturedProduct)) {
            CapturedProductState state = capturedProducts.get(capturedProduct);
            state.increment();
            return ResponseEntity.ok(updateCartState(state));
        }
        throw new BadRequestException();
    }
    
    /**
     * Remove from cart
     * @return
     */
    @PostMapping("${url.cart.remove}")
    @ResponseBody
    public ResponseEntity<CartState<CapturedProduct>> removeFromCart(@RequestBody CapturedProduct capturedProduct) {
        if (capturedProducts.containsKey(capturedProduct)) {
            capturedProducts.remove(capturedProduct);
            return ResponseEntity.ok(updateCartState(capturedProduct));
        }
        throw new BadRequestException();
    }
    
    /**
     * Add to cart
     * @return
     */
    @PostMapping("${url.cart.add}")
    @ResponseBody
    public ResponseEntity<CartState<CapturedProduct>> addToCart(@RequestBody CapturedProduct capturedProduct) {
        capturedProduct = productService.getProductCartDetails(capturedProduct);
        if (capturedProduct != null) {
            capturedProducts.computeIfAbsent(capturedProduct, cp -> new CapturedProductState(cp.getProductId(), cp.getPrice())).increment();
            return ResponseEntity.ok(updateCartState(capturedProduct));
        } throw new BadRequestException();
    }
    
}
