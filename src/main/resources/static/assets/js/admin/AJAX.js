var doAJAX = function(URL, data, successFunction){
    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
    var csrfToken = $("meta[name='_csrf']").attr("content");
    var headers = {};
    headers[csrfHeader] = csrfToken;

    $.ajax({
        url: URL,
        type: 'POST',
        // headers: headers,
        async: true,
        contentType: "application/json",
        data: data,
        success: function (response) {
            successFunction(response);
        }
    });

};

