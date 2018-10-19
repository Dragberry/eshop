function addToCart(action, id) {
	var productToAdd = {
			action: action,
			entityId: id
	};
	$.ajax({
		url: document.getElementById('addToCartForm').action,
	    data: JSON.stringify(productToAdd),
    	type: 'PATCH',
        contentType: 'application/json',
        dataType: 'json',
        cache: false,
        success: function(cartState)  {
			$('#addToCartModal').replaceWith(cartState.value);
			$('#addToCartModal').modal('show');
			refreshCartCount(cartState);
		}
    });
}

/**
 * Call this method to add selected product option on the select-product-modal
 * It's shows the add-to-cart-success-modal, when the select-product-modal is already hidden
 */
function addProductToCart() {
	$('#addToCartModal').on('hidden.bs.modal', function (e) {
		addToCart('ADD_PRODUCT', $('input[name="productId"]:checked').val());
	});
	$('#addToCartModal').modal('hide');
}

/**
 * This method is called when the user select a product option on the select-product-modal
 */
function onProductOptionChange() {
	$('.select-product-option:has(input[name="productId"]:checked)').addClass('list-group-item-success')
		.siblings().removeClass('list-group-item-success')
	$('input[name="productId"]').siblings().addClass('invisible');
	$('input[name="productId"]:checked').siblings().removeClass('invisible');
}