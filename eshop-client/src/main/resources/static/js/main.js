/* on page init for mobiles */
$(document).on('pageinit', function() {
	onPageResize();
	calculateRatings();
	updateCommentsCount();
});
/* On document ready */
$(document).ready(function() {
	onPageResize();
	$(window).resize(onPageResize);
	calculateRatings();
	updateCommentsCount();
	preventClosingQuickSearchPanel();
});

$(document).ajaxError(showServerErrorModal);

/**
 * Prevent to close quick search panel if it's clicked inside
 */
function preventClosingQuickSearchPanel() {
//	$(document).on('click', '#searchResults', function (e) {
//		e.stopPropagation();
//	});
}

/*
 * This method should be used as success ajax function when server can return validation errors
 */
function processResult(result, success) {
	if (result.issues.length === 0) {
		success(result.value);
	} else {
		showValidationErrors(result.issues);
	}
}

/* Show server validation messages */
function showValidationErrors(issues) {
	issues.forEach(function(issue) {
		$fieldId = $('#' + issue.fieldId).addClass('is-invalid');
		$fieldId.after($('<label></label>')
			.attr('id', $fieldId.attr('id') + '-error')
			.attr('for', $fieldId.attr('id'))
			.addClass('invalid-feedback d-block')
			.text(issue.message));
	});
}

/* Update body margin on screen resize */
function onPageResize() {
	var headerHeight = $('header').height();
	var bodyMarginTop = $('body').css('margin-top');
	if (bodyMarginTop != headerHeight) {
		$("body").css('margin-top', headerHeight);
	}
	
	var bodyMarginBottom = $('body').css('margin-bottom');
	var footerHeight = $('footer').height();
	if (bodyMarginBottom + 50 != footerHeight) {
		$("body").css('margin-bottom', footerHeight + 50);
	}
}
/* Calculate ratings */
function calculateRatings() {
	$('.star-rating').each(function() {
		$stars = $(this).find('.fa-star');
		$stars.each(function(){
			if (Math.round($stars.siblings('input.rating-value').val()) >= parseInt($(this).data('rating'))) {
		      return $(this).addClass('active');
		    } else {
		      return $(this).removeClass('active');
			}
		});
	});
}

/* Builds title for comments count */
$.fn.buildCommentTitle = function(num, suffix) {
  return num == 0 ? 'Нет отзывов' : num + ' ' + suffix;
}

/* Builds title for comments count */
$.fn.getCommentTitle = function(num) {
  var str = '' + num;
  if (str.endsWith('10') || str.endsWith('11') || str.endsWith('12') || str.endsWith('14')) {
   return $.fn.buildCommentTitle(num, 'отзывов');
  }
  if (str.endsWith('1')) {
   return $.fn.buildCommentTitle(num, 'отзыв');
  }
  if (str.endsWith('2') || str.endsWith('3') || str.endsWith('4')) {
   return $.fn.buildCommentTitle(num, 'отзыва');
  }
  return $.fn.buildCommentTitle(num, 'отзывов');
}

/* Updates comments count */
function updateCommentsCount() {
	$('.product-comments-count').each(function() {
		$(this).text($.fn.getCommentTitle($(this).data('value')));
	});
}

/* Show an error model window */
function showServerErrorModal() {
	$('#serverErrorModal').modal('show');
}

/**
 * Refresh cart count when the product is added to cart
 */
function refreshCartCount(cartState) {
	$(".cart-product-count").text(cartState.quantity == 0 ? '' : cartState.quantity);
}

/**
 * Performs a quick search with  delay
 */
var delaySearchTimer;
function doSearch(event) {
	clearTimeout(delaySearchTimer);
	delaySearchTimer = setTimeout(function() {
		var form = $('#searchForm');
	    $.ajax({
	    	type: 'GET',
	        url: quickSearchUrl,
	        data: form.serialize(),
	        success: function(data)  {
	        	$('#searchResults').removeClass('show');
    			$('#searchResults').html(data);
    			calculateRatings();
    			updateCommentsCount();
    			$('#searchResults').addClass('show');
	        }
	   	});
	}, 250);
}

/**
 * Performs a filter operation on search screen
 */
var delaySearchFilterTimer;
function doFilter() {
	clearTimeout(delaySearchFilterTimer);
	delaySearchFilterTimer = setTimeout(function() {
		var form = $('#filtersForm');
	    var url = form.attr('action');
	    $.ajax({
	    	type: "GET",
	        url: url,
	        data: form.serialize(),
	        success: function(data)  {
	        	$('#productList').fadeOut('normal', function() {
	        		$(this).replaceWith(data);
	        		calculateRatings();
	        		updateCommentsCount();
	        	});
	        }
	   	});
	}, 1000);
}

function closeQuickSearchResults() {
	$('#searchResults').removeClass('show');
}

/**
 * Change mobile quick search input on focus
 */
function onQuickSearchFocus() {
	$('#headerCartColumn, #headerMenuColumn').addClass('d-none');
	$('#headerSearchColumn').addClass('col-12').removeClass('col-6');
}

/**
 * Change mobile quick search input on losing focus
 */
function onQuickSearchBlur() {
	$('#headerCartColumn, #headerMenuColumn').removeClass('d-none');
	$('#headerSearchColumn').removeClass('col-12').addClass('col-6');
}