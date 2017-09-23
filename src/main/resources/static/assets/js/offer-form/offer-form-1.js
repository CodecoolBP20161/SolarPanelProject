$(document).ready(function () {

    var metricSelect = $('#stepOneSelect');
    var metricInput = $('#sub');

    var phaseOneInput = $('#phase1RadioInput');
    var phaseTwoInput = $('#phase2RadioInput');

    var formattedInputValue = formatPrice(metricInput.val());

    if (metricInput.val() == "0.0" || formattedInputValue == '0'){
        metricInput.val("")
    } else {
        (metricInput.val(formattedInputValue));
    }

    submitted = false;

    metricSelect.on('ready change', function () {
        $('#metricHeader').html(metricSelect.find('option:selected').val());
    });

    metricSelect.add(metricInput).add(phaseOneInput).add(phaseTwoInput)
        .on('ready change paste keyup', function () {
            var formattedInput = formatPrice(metricInput.val());

            var futureInput = isNumberInputFieldValueValid(metricInput.val()) ? formattedInput : '' ;

            metricInput.val(futureInput);

            isNetworkUpgradeNeeded(phaseOneInput, metricSelect, metricInput);
        });

    $('#submit').on('click', function(){
        var inputValue = metricInput.val();

        // debugger;
        if (inputValue === "" || inputValue == 0){
            metricInput.val(null);
        } else {
            metricInput.val(unFormatPrice(metricInput.val()));
        }

    });

});

var isNetworkUpgradeNeeded = function(phaseOneInput, metricSelect, metricInput){
    var alertSpan = $('#alert-phase');
    if (phaseOneInput.val() == "1"){
        var metric = metricSelect.find('option:selected').val();
        var value = unFormatPrice(metricInput.val());

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
                if (needed == "true") {
                    alertSpan.prop('hidden', false);

                } else {
                    alertSpan.prop('hidden', true);
                }
            }
        });
    }

};
