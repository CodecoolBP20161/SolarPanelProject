$(document).ready(function () {

    var metricSelect = $('#stepOneSelect');
    var metricInput = $('#sub');
    var phaseOneInput = $('#phase1RadioInput');
    var phaseTwoInput = $('#phase2RadioInput');
    if (metricInput.val() == "0.0") metricInput.val("");
    else (metricInput.val(accounting.formatNumber(metricInput.val(), {precision : 0, thousand : " "})));

    // Changes the metric type accordingly
    metricSelect.on('ready change', function () {
        $('#metricHeader').html(metricSelect.find('option:selected').val());
    });

    // This one just adds the same event listener to the 4 elements
    metricSelect.add(metricInput).add(phaseOneInput).add(phaseTwoInput)
        .on('ready change paste keyup', function () {
            var formattedInput = accounting.formatNumber(metricInput.val(), {precision : 0, thousand : " "});
            if (metricInput.val() != ''){
                console.log("nem egyenlő");
                metricInput.val(formattedInput);
            }
            isNetworkUpgradeNeededAdmin(phaseOneInput, metricSelect, metricInput);
        });

    $('#submit').on('click', function(){
        metricInput.val(accounting.unformat(metricInput.val()));
    });

});

var isNetworkUpgradeNeededAdmin = function(phaseOneInput, metricSelect, metricInput){
    var alertSpan = $('#alert-phase');
    if (phaseOneInput.val() == "1"){
        var metric = metricSelect.find('option:selected').val();
        var value = accounting.unformat(metricInput.val());

        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var headers = {};
        headers[csrfHeader] = csrfToken;
        $.ajax({
            url: "/admin/network-upgrade",
            type: 'POST',
            async: true,
            headers: headers,
            contentType: "application/json",
            data: JSON.stringify({
                "value": value,
                "metric": metric
            }),
            success: function (needed) {
                console.log("needed: " + needed);
                if (needed == "true") {
                    alertSpan.prop('hidden', false);

                } else if (needed == "false") {
                    alertSpan.prop('hidden', true);
                }
            }
        });
    }

};
