/* on page init for mobiles */
$(document).on('pageinit', function() {
	onPageResize();
	calculateRatings();
	updateCommentsCount();
});
/* On documnet ready */
$(document).ready(function() {
	onPageResize();
	$(window).resize(onPageResize);
	calculateRatings();
	updateCommentsCount();
});

/* Update body margin on screen resize */
function onPageResize() {
	var bodyMargin = $('body').css('margin-bottom');
	var footerHeight = $('footer').height();
	if (bodyMargin + 50 != footerHeight) {
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