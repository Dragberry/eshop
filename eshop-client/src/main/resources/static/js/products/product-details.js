function onProductOptionChanged(el) {
	updatePrice();
	changeImage(el);
}

function changeImage(el) {
	var $el = $(el);
	var $imgIndex = $('li[data-img*="' + $el.data('option-name') + '_' + $el.find('option:selected').text() + '"]').data('slide-to');
	if ($imgIndex) {
		$('#productCarouselIndicators').carousel($imgIndex);
	}
}

function isProductMatch(optionName, optionValue) {
	var isMatched = false;
	$('select[data-option-name]').each(function() {
		if (optionName == $(this).data('option-name') && optionValue == $(this).find('option:selected').val()) {
			isMatched = true;
			return false;
		}
	});
	return isMatched;
}

function getProductId() {
	for (productId in product.productOptions) {
		// if product doesn't have options, then choose the first one
		if (product.productOptions[productId].length == 0) {
			return productId;
		} else {
			// if product has options, then choose product using the select option set
		    for (optionSetIndex in product.productOptions[productId]) {
		    	if (isProductMatch(product.productOptions[productId][optionSetIndex]['key'], product.productOptions[productId][optionSetIndex]['value'])) {
		    		return productId;
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

// this functions sets the product price based on selected options
function updatePrice() {
	var productId = getProductId();
	var actualPrice = product.productActualPrices[productId];
	if (actualPrice == null) {
		actualPrice = 0;					
		$('.product-actual-price-block').addClass('d-none');
		$('.product-price-block').removeClass('d-none');
	} else {
		$('.product-actual-price-block').removeClass('d-none');
		$('.product-price-block').addClass('d-none');
	}
	$('.product-actual-price').text(actualPrice.toFixed(2));	
	$('.product-price').text(product.productPrices[productId].toFixed(2));
}

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
		messages: {
			name: {
				required: '[[#{msg.error.comment.name.required}]]',
				maxlength: '[[#{msg.error.comment.name.tooLong}]]',
			},
			email: {
				required: '[[#{msg.error.common.email.required}]]',
				email: '[[#{msg.error.common.email.invalid}]]',
				maxlength: '[[#{msg.error.common.email.tooLong}]]'
			},
			comment: {
				required: '[[#{msg.error.comment.comment.required}]]',
				maxlength: '[[#{msg.error.comment.comment.tooLong}]]'
			},
			productRating: {
				required: '[[#{msg.error.comment.rating.required}]]',
				min: '[[#{msg.error.comment.rating.invalid}]]',
				max: '[[#{msg.error.comment.rating.invalid}]]',
			}
		},
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
						$('#addComment').text('[[#{msg.common.thanksForComment}]]').addClass('mt-4 mb-4 alert alert-success');
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
		messages: {
			phone: {
				required: '[[#{msg.error.contactPhoneRequired}]]',
				maxlength: '[[#{msg.error.contactPhoneIsTooLong}]]'
			},
			fullName: {
				maxlength: '[[#{msg.error.fullNameIsTooLong}]]'
			},
			address: {
				maxlength: '[[#{msg.error.addressIsTooLong}]]'
			}
		},
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