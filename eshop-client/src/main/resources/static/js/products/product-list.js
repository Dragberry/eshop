function onDisplayChanged() {
	$('input[name="display"]').parent().removeClass('btn-secondary');
	$('input[name="display"]:checked').parent().addClass('btn-secondary');
}

var delayTimer;

function doFilter() {
	countFilters();
	clearTimeout(delayTimer);
	delayTimer = setTimeout(function() {
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

function resetForm() {
	document.getElementById('filtersForm').reset()
	$('#filtersForm input[type="text"]').val('');
	$('#filtersForm input[type="checkbox"]').attr('checked', false);
	$('#sorting').prop('selectedIndex', 0);
	doFilter();
}

function countFilters() {
	var count = $('#filtersForm input[type="text"]').filter(function() {
	    return this.value;
	}).length + $('#filtersForm input[type="checkbox"]:checked').length;
	if (count == 0) {
		$('.all-filters-count').addClass('d-none').text(0);
		$('.all-filters-reset-button').addClass('d-none');
	} else {
		$('.all-filters-count').removeClass('d-none').text(count);
		$('.all-filters-reset-button').removeClass('d-none');
	}
}