function toOrdering() {
	$('#cartContent').load(cartNextUrl, function() {
		prepareForm();
	});
}

function toEditing() {
	$('#cartContent').load(cartBackUrl);
}

function incrementCartItem(productId) {
	changeCartItemQuantity(productId, 'INCREMENT');
}

function decrementCartItem(productId) {
	changeCartItemQuantity(productId, 'DECREMENT');
}

function changeCart(action, entityId, onsuccess) {
	$.ajax({
		url: cartStateUrl,
	    data: JSON.stringify({action: action, entityId: entityId}),
    	type: 'PATCH',
        contentType: 'application/json',
        dataType: 'json',
        cache: false,
        success: onsuccess
    });
}

function removeItemFromCart(productId) {
	changeCart('REMOVE_PRODUCT', productId, function(cartState) {
    	$('*[data-product-id="' + productId + '"]').fadeOut("normal", function() {
    		$(this).remove();
    		if ($('*[data-product-id]').length <= 0) {
    			$('#cartIsEmpty').removeClass('d-none');
    			$('#cartItems').remove();
    		} else {
        		$("*[data-index]").each(function(index) {
            		$(this).text(index + 1);
            	})
    		}
    	});
    	onCartChanges(cartState);
    });
}

function changeCartItemQuantity(productId, action) {
	changeCart(action, productId, function(cartState) {
		$('*[data-product-id="' + productId + '"]').find('.cart-product-quantity').find('input').val(cartState.value.quantity); 
		$('*[data-product-id="' + productId + '"]').find('.cart-product-item-total-amount').text(cartState.value.totalAmount.toFixed(2))
    	onCartChanges(cartState);
    });
}

function onCartChanges(cartState) {
	refreshCartCount(cartState);
	refreshCartTotalAmount(cartState);
	changeToOrderButtonState(cartState);
}

function changeToOrderButtonState(cartState) {
	$('#toOrderButton').prop('disabled', cartState.quantity <= 0);
}

function refreshCartTotalAmount(cartState) {
	$(".cart-product-total-amount").text(cartState.totalProductAmount.toFixed(2));
	$(".cart-shipping-cost").text(cartState.shippingCost.toFixed(2));
	$(".cart-total-amount").text(cartState.totalAmount.toFixed(2));
}

function prepareForm() {
	$('#orderDetailsPhone').mask('+375(00)000-00-00');
	
	$('input[type=radio][name=shippingMethod]').change(function() {
		changeCart('SHIPPING_METHOD', $(this).val(), function(cartState) {
        	onCartChanges(cartState);
        });
	});
	
	$('#submitOrderForm').validate({
		debug: true,
		errorClass: 'invalid-feedback',
		validClass: 'valid-feedback',
		rules: {
			phone: {
				required: true,
				maxlength: 20
			},
			fullName: {
				maxlength: 64
			},
			shippingMethod: 'required',
			paymentMethod: 'required',
			email: {
				email: true,
				maxlength: 128
			},
			address: {
				maxlength: 128
			},
			comment: {
				maxlength: 128
			}
		},
		messages: validationMessages,
		submitHandler: function(form) {
			
			var orderDetails = {
				'phone': $(form).find('input[name="phone"]').val(),
				'fullName': $(form).find('input[name="fullName"]').val(),
				'address': $(form).find('textarea[name="address"]').val(),
				'email': $(form).find('input[name="email"]').val(),
				'comment': $(form).find('textarea[name="comment"]').val(),
				'shippingMethod': $(form).find('input[name="shippingMethod"]:checked').val(),
				'paymentMethod': $(form).find('input[name="paymentMethod"]:checked').val()
			}
			
			$.ajax({
				url: form.action,
				type: form.method,
				data: JSON.stringify(orderDetails),
				contentType: "application/json",
				success: function(response) {
					$('#cartContent').html(response);
					prepareForm();
				}
			});
		},
		highlight: function(element, errorClass, validClass) {
	    	$(element.form).find("input[name=" + element.name + "]")
      			.addClass('is-invalid').removeClass('is-valid');
	    	$(element.form).find("label[for=" + element.name + "]")
	      		.addClass('invalid-feedback');
	  	},
	  	unhighlight: function(element, errorClass, validClass) {
	  		$(element.form).find("input[name=" + element.name + "]")
  				.removeClass('is-invalid').addClass('is-valid');
	    	$(element.form).find("label[for=" + element.name + "]")
	      		.removeClass('invalid-feedback');
	  	}
	});
}

$(document).ready(function() {
	prepareForm();
});