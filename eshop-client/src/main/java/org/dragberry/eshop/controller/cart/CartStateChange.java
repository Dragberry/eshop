package org.dragberry.eshop.controller.cart;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an change request for a cart
 * @author Drahun Maksim
 *
 */
@Setter
@Getter
public class CartStateChange {
    private CartAction action;
    private Long entityId;
    private Long value;
}