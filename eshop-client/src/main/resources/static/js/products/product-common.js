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