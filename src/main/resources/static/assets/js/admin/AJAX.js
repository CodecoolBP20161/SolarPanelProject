
var doAJAX = function(URL, data, successFunction, isCSRFNeeded){
    if (isCSRFNeeded == undefined) isCSRFNeeded = false;
    if (isCSRFNeeded){
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var headers = {};
        headers[csrfHeader] = csrfToken;
    }

    $.ajax({
        url: URL,
        type: 'POST',
        headers: isCSRFNeeded ? headers : undefined,
        async: true,
        contentType: "application/json",
        data: data,
        success: function (response) {
            successFunction(response);
        }
    });
};

var isNumberInputFieldValueValid = function (value) {
    return value != '' && value != '0';
};

var formatPrice = function(rawPrice){
    return accounting.formatNumber(rawPrice, {precision : 0, thousand : " "})
};

var unFormatPrice = function (formattedPrice) {
    return accounting.unformat(formattedPrice);
};

