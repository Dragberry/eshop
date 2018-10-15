function onProductOptionChanged(el) {
	updatePrice();
	changeImage(el);
}

//this functions sets the product price based on selected options
function updatePrice() {
	var productId = getProductId();
	var actualPrice = products.productActualPrices[productId];
	if (actualPrice == null) {
		actualPrice = 0;					
		$('.product-actual-price-block').addClass('d-none');
		$('.product-price-block').removeClass('d-none');
	} else {
		$('.product-actual-price-block').removeClass('d-none');
		$('.product-price-block').addClass('d-none');
	}
	$('.product-actual-price').text(actualPrice.toFixed(2));	
	$('.product-price').text(products.productPrices[productId].toFixed(2));
}

function changeImage(el) {
	var $el = $(el);
	var $imgIndex = $('li[data-img*="' + $el.data('option-name') + '_' + $el.find('option:selected').text() + '"]').data('slide-to');
	if ($imgIndex) {
		$('#productCarouselIndicators').carousel($imgIndex);
	}
}

function isProductMatchRadio(optionName, optionValue) {
	return optionValue == $('input[name="' + optionName + '"]:checked').val();
}

function isProductMatchSelect(optionName, optionValue) {
	var isMatched = false;
	$('*[data-option-name]').each(function() {
		if (optionName == $(this).data('option-name') && optionValue == $(this).find('option:selected').val()) {
			isMatched = true;
			return false;
		}
	});
	return isMatched;
}

function getProductId() {
	for (productId in products.productOptions) {
		// if product doesn't have options, then choose the first one
		if (products.productOptions[productId].length == 0) {
			return productId;
		} else {
			// if product has options, then choose product using the select option set
		    var optionSet = products.productOptions[productId];
		    if (optionSet.length == 1) {
		    	// If the product can have only one option
		    	for (optionSetIndex in products.productOptions[productId]) {
		    		if (isProductMatchRadio(products.productOptions[productId][optionSetIndex]['key'], products.productOptions[productId][optionSetIndex]['value'])) {
			    		return productId;
			    	}
		    	}
			} else {
				// If the product can have several options
				for (optionSetIndex in products.productOptions[productId]) {
			    	if (isProductMatchSelect(products.productOptions[productId][optionSetIndex]['key'], products.productOptions[productId][optionSetIndex]['value'])) {
			    		return productId;
			    	}
			    }
			}
		}
	}
}

$(document).ready(function() {
	prepareAddCommentForm();
	prepareQuickOrderForm();
	updatePrice();
});

// ***** Product comment and rating ***** //
function onProductRate(rating) {
	$('input[name="productRating"]').val(rating);
}

function onHoverRate(el) {
	var $rating = $('input[name="productRating"]');
	$rating.siblings('.fa-star').each(function() {
		if ($(this).data('rating') <= $(el).data('rating')) {
			$(this).addClass('active');
		} else {
			$(this).removeClass('active');
		}
	})
}

function onInterruptedRate() {
	var $rating = $('input[name="productRating"]');
	$rating.siblings('.fa-star').each(function() {
		if ($(this).data('rating') <= $rating.val()) {
			$(this).addClass('active');
		} else {
			$(this).removeClass('active');
		}
	})
}

function prepareAddCommentForm() {
	$('#addCommentForm').validate({
		debug: true,
		errorClass: 'invalid-feedback',
		validClass: 'valid-feedback',
		ignore: [],
		rules: {
			name: {
				required: true,
				maxlength: 32
			},
			email: {
				required: true,
				email: true,
				maxlength: 128
			},
			comment: {
				required: true,
				maxlength: 1024
			},
			productRating: {
				required: true,
				min: 1,
				max: 5
			}
		},
		messages: addCommentValidationMessages,
		submitHandler: function(form) {
			$('#submitProductCommentButton').prop('disabled', true);
			var comment = {
				'productId': $(form).find('input[name="productId"]').val(),
				'name': $(form).find('input[name="name"]').val(),
				'email': $(form).find('input[name="email"]').val(),
				'text': $(form).find('textarea[name="comment"]').val(),
				'mark': $(form).find('input[name="productRating"]').val()
			}
			$.ajax({
				url: form.action,
				type: form.method,
				data: JSON.stringify(comment),
				contentType: "application/json",
				success: function(result) {
					processResult(result, function(value) {
						$('#productCommentZero').after(result.value);
						$('#addComment').text(addCommentSuccessMessage).addClass('mt-4 mb-4 alert alert-success');
						calculateRatings();
					});
				},
				complete: function() {
					$('#submitProductCommentButton').prop('disabled', false);
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

function showQuickOrderModal() {
	$('#quickOrderModal').modal("show");
}

function prepareQuickOrderForm() {
	$('#orderDetailsPhone').mask('+375(00) 000-00-00');
	
	$('#quickOrderForm').validate({
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
			address: {
				maxlength: 128
			}
		},
		messages: quickOrderValidationMessages,
		submitHandler: function(form) {
			var orderDetails = {
				'phone': $(form).find('input[name="phone"]').val(),
				'fullName': $(form).find('input[name="fullName"]').val(),
				'address': $(form).find('textarea[name="address"]').val(),
				'productId': getProductId()
			}
			$.ajax({
				url: form.action,
				type: form.method,
				data: JSON.stringify(orderDetails),
				contentType: "application/json",
				success: function(response) {
					$('#quickOrderModal').modal("hide");
					$('#quickOrderSuccessModal').modal("show");
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