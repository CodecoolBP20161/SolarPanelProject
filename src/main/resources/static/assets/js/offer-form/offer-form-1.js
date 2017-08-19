$(document).ready(function () {

    var metricSelect = $('#stepOneSelect');
    var metricInput = $('#sub');
    var phaseOneInput = $('#phase1RadioInput');
    var phaseTwoInput = $('#phase2RadioInput');
    var alertSpan = $('#alert-phase');
    if (metricInput.val() == "0.0") metricInput.val("");
    else (metricInput.val(accounting.formatNumber(metricInput.val(), {precision : 0, thousand : " "})));
    submitted = false;

    // Changes the metric type accordingly
    metricSelect.on('ready change', function () {
        $('#metricHeader').html(metricSelect.find('option:selected').val());
    });

    // This one just adds the same event listener to the 4 elements
    metricSelect.add(metricInput).add(phaseOneInput).add(phaseTwoInput)
        .on('ready change paste keyup', function () {
            metricInput.val(accounting.formatNumber(metricInput.val(), {precision : 0, thousand : " "}));
            isNetworkUpgradeNeeded(phaseOneInput, metricSelect, metricInput);
        });

    $('#submit').on('click', function(){
        metricInput.val(accounting.unformat(metricInput.val()));
    });

});

var isNetworkUpgradeNeeded = function(phaseOneInput, metricSelect, metricInput){
    var alertSpan = $('#alert-phase');
    if (phaseOneInput.val() == "1"){
        var metric = metricSelect.find('option:selected').val();
        var value = metricInput.val().replace(".", "");

        $.ajax({
            url: "/ajanlat/network-upgrade",
            type: 'POST',
            async: true,
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
