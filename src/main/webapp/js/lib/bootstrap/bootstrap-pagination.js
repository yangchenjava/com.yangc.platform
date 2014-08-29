(function($){
	$.showPagination = function(pageCount, pageNow, pagination, callback){
		var showPageCount = 5;
		var i = 1;
		while (true) {
			if (pageNow <= i * showPageCount) {
				var pa = "<ul>";
				if (pageNow == 1) {
					pa += "<li class='disabled'><a href='javascript:void(0);'>&laquo;</a></li>";
				} else {
					pa += "<li><a href='javascript:" + callback + "(" + (pageNow - 1) + ");'>&laquo;</a></li>";
				}
				if (pageCount >= i * showPageCount) {
					for (var j = (i - 1) * showPageCount + 1; j <= i * showPageCount; j++) {
						if (j == pageNow) {
							pa += "<li class='active'><a href='javascript:" + callback + "(" + j + ");'>" + j + "</a></li>";
						} else {
							pa += "<li><a href='javascript:" + callback + "(" + j + ");'>" + j + "</a></li>";
						}
					}
				} else {
					for (var j = (i - 1) * showPageCount + 1; j <= pageCount; j++) {
						if (j == pageNow) {
							pa += "<li class='active'><a href='javascript:" + callback + "(" + j + ");'>" + j + "</a></li>";
						} else {
							pa += "<li><a href='javascript:" + callback + "(" + j + ");'>" + j + "</a></li>";
						}
					}
				}
				if (pageNow == pageCount) {
					pa += "<li class='disabled'><a href='javascript:void(0);'>&raquo;</a></li>";
				} else {
					pa += "<li><a href='javascript:" + callback + "(" + (pageNow + 1) + ");'>&raquo;</a></li>";
				}
				pa += "</ul>";
				pagination.html(pa);
				break;
			}
			i++;
		}
	};
})(jQuery);
