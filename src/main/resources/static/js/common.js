
$(document).ready(function(){
	 $("body").tooltip({ selector: '[data-toggle=tooltip]' });
});
//request call function
$.fn.requestCall = function(type, data, url, timeout, successCallback) {
	// Call the search service
    $.ajax({
		url: url,
        type: type,
        contentType:'application/json',
        dataType: 'text',
		data: data,
        timeout: timeout,
        success: function (data) {   // success callback function
	        console.log('Request Success: ');
	        successCallback(data);
	    },
	    error: function (jqXhr, textStatus, errorMessage) { // error callback 
	       console.log('Request Fail'+errorMessage+textStatus+jqXhr);
	    }
    }).then(function(data) {
		console.log("request Call post action");
    });	
}
