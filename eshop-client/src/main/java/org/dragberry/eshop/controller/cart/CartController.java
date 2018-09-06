package org.dragberry.eshop.controller.cart;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.dragberry.eshop.common.IssueTO;
import org.dragberry.eshop.common.ResultTO;
import org.dragberry.eshop.controller.exception.BadRequestException;
import org.dragberry.eshop.model.cart.CapturedProduct;
import org.dragberry.eshop.model.cart.CapturedProductState;
import org.dragberry.eshop.model.cart.CartState;
import org.dragberry.eshop.model.cart.OrderDetails;
import org.dragberry.eshop.model.delivery.DeliveryMethod;
import org.dragberry.eshop.model.payment.PaymentMethod;
import org.dragberry.eshop.service.AppInfoService;
import org.dragberry.eshop.service.DeliveryService;
import org.dragberry.eshop.service.NotificationService;
import org.dragberry.eshop.service.OrderService;
import org.dragberry.eshop.service.PaymentService;
import org.dragberry.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    private AppInfoService appInfoService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private DeliveryService deliveryService;
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private HttpSession session;
    
    @Autowired
    private HttpServletRequest request;
    
    private OrderDetails order = new OrderDetails();
    
    private CartStep cartStep = CartStep.EDITING;
    
    private List<DeliveryMethod> deliveryMethods;
    
    private List<PaymentMethod> paymentMethods;
    
    /**
     * Initialize
     */
    @PostConstruct
    public void init() {
    	deliveryMethods = deliveryService.getDeliveryMethods();
    	paymentMethods = paymentService.getPaymentMethods();
    }

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
        mv.addObject("order", order);
        mv.addObject("cartStep", cartStep);
        mv.addObject("deliveryMethods", deliveryMethods);
        mv.addObject("paymentMethods", paymentMethods);
        updateCartState();
        return mv;
    }
    
    @PatchMapping("${url.cart.state}")
    @ResponseBody
    public CartState<?> updateCart(@RequestBody CartStateChange change) {
        return executors.get(change.getAction()).execute(change);
    }
    
    /**
     * Go to ordering page
     * @return
     */
    @PostMapping("${url.cart.submit-order}")
    public ModelAndView submitOrder() {
        log.info("New order has been submitted!");
        saveOrderDetails();
        ResultTO<OrderDetails> result = orderService.createOrder(order);
        List<IssueTO> issues = result.getIssues();
        if (issues.isEmpty()) {
            CartState<OrderDetails> cartState = updateCartState(result.getValue());
            Map<String, Object> notificationParams = Map.of(
                    "order", cartState.getValue(),
                    "productSum", cartState.getProductSum(),
                    "deliveryPrice", cartState.getDeliveryPrice(),
                    "sum", cartState.getSum(),
                    "shopName", appInfoService.shopName());
            notificationService.sendNotification(appInfoService.getSystemInfo().getEmail(), "order-report", notificationParams);
            if (StringUtils.isNotBlank(order.getEmail())) {
                notificationService.sendNotification(order.getEmail(), "order-customer-report", notificationParams);
            }
            order = new OrderDetails();
            cartStep = CartStep.SUCCESS;
            log.info("New order has been created!");
        } else {
            log.warn(MessageFormat.format("Validation fails during order creation: {0}", issues));
        }
        ModelAndView mv = new ModelAndView(cartStep.template);
        mv.addObject("order", order);
        mv.addObject("validationErrors", issues);
        mv.addObject("deliveryMethods", deliveryMethods);
        mv.addObject("paymentMethods", paymentMethods);
        updateCartState();
        return mv;
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
        mv.addObject("order", order);
        mv.addObject("deliveryMethods", deliveryMethods);
        mv.addObject("paymentMethods", paymentMethods);
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
        mv.addObject("order", order);
        updateCartState();
        return mv;
    }
    
    /**
     * Save order details from the request
     */
    public void saveOrderDetails() {
    	order.setPhone(request.getParameter("phone"));
    	order.setFullName(request.getParameter("fullName"));
    	order.setAddress(request.getParameter("address"));
    	order.setEmail(request.getParameter("email"));
    	order.setComment(request.getParameter("comment"));
    	try {
    	    Long deliveryMethodKey = Long.valueOf(request.getParameter("deliveryMethod"));
    	    order.setDeliveryMethod(deliveryMethods.stream().filter(dm -> dm.getId().equals(deliveryMethodKey)).findFirst().orElse(null));
    	    Long paymentMethodKey = Long.valueOf(request.getParameter("paymentMethod"));
    	    order.setPaymentMethod(paymentMethods.stream().filter(pm -> pm.getId().equals(paymentMethodKey)).findFirst().orElse(null));
    	} catch (NumberFormatException nfe) { /* Do nothing */ }
    }
    
    /**
     * Updates product count and sum in session. Wraps result in holder
     * @param value
     */
    private <T> CartState<T> updateCartState(T value) {
        var cartState = new CartState<T>();
        int quantity = order.getProducts().values().stream()
                .mapToInt(CapturedProductState::getQuantity).sum();
        cartState.setQuantity(quantity);
        session.setAttribute("cartProductCount", quantity);
        BigDecimal productSum = order.getProducts().values().stream()
                .map(CapturedProductState::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        cartState.setProductSum(productSum);
        session.setAttribute("cartProductSum", productSum);
        
        cartState.setDeliveryPrice(order.getDeliveryMethod() == null ? BigDecimal.ZERO : order.getDeliveryMethod().getPrice());
        session.setAttribute("cartDeliveryPrice", cartState.getDeliveryPrice());
        cartState.setSum(productSum.add(cartState.getDeliveryPrice()));
        session.setAttribute("cartTotalSum", cartState.getSum());
        
        cartState.setValue(value);
        return cartState;
    }
    
    /**
     * Updates product count and sum in session
     */
    private CartState<?> updateCartState() {
        return updateCartState(null);
    }
    
    /**
     * Executor for  change cart action
     * @author Drahun Maksim
     *
     * @param <T>
     */
    interface CartActionExecutor<T> {
        /**
         * Executes a change cart action
         * @param change
         * @return
         */
        CartState<T> execute(CartStateChange change);
    }
    
    private Map<CartAction, CartActionExecutor<?>> executors = new HashMap<>();
    {
      executors.put(CartAction.ADD_PRODUCT, new CartActionExecutor<CapturedProduct>() {

        @Override
        public CartState<CapturedProduct> execute(CartStateChange change) {
            CapturedProduct capturedProduct = productService.getProductCartDetails(change.getEntityId());
            if (capturedProduct != null) {
                order.getProducts().computeIfAbsent(capturedProduct, cp -> new CapturedProductState(cp.getProductId(), cp.getPrice())).increment();
                return updateCartState(capturedProduct);
            } throw new BadRequestException();
        }
      });
      
      executors.put(CartAction.REMOVE_PRODUCT, new CartActionExecutor<CapturedProduct>() {

          @Override
          public CartState<CapturedProduct> execute(CartStateChange change) {
              CapturedProduct product = new CapturedProduct();
              product.setProductId(change.getEntityId());
              if (order.getProducts().containsKey(product)) {
                  order.getProducts().remove(product);
                  return updateCartState(product);
              }
              throw new BadRequestException();
          }
        });
      
      executors.put(CartAction.INCREMENT, new CartActionExecutor<CapturedProductState>() {

          @Override
          public CartState<CapturedProductState> execute(CartStateChange change) {
              CapturedProduct product = new CapturedProduct();
              product.setProductId(change.getEntityId());
              if (order.getProducts().containsKey(product)) {
                  CapturedProductState state = order.getProducts().get(product);
                  state.increment();
                  return updateCartState(state);
              }
              throw new BadRequestException();
          }
        });
      
      executors.put(CartAction.DECREMENT, new CartActionExecutor<CapturedProductState>() {

          @Override
          public CartState<CapturedProductState> execute(CartStateChange change) {
              CapturedProduct product = new CapturedProduct();
              product.setProductId(change.getEntityId());
              if (order.getProducts().containsKey(product)) {
                  CapturedProductState state = order.getProducts().get(product);
                  state.decrement();
                  return updateCartState(state);
              }
              throw new BadRequestException();
          }
        });
      
      executors.put(CartAction.DELIVERY_METHOD, new CartActionExecutor<DeliveryMethod>() {

          @Override
          public CartState<DeliveryMethod> execute(CartStateChange change) {
              DeliveryMethod method = deliveryMethods.stream().filter(dm -> dm.getId().equals(change.getEntityId())).findFirst()
                      .orElseThrow(BadRequestException::new);
              order.setDeliveryMethod(method);
              return updateCartState(method);
          }
        });
    }
}
