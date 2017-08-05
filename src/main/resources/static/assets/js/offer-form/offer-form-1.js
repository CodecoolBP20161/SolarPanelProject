$(document).ready(function () {

    var metricSelect = $('#stepOneSelect');
    var metricInput = $('#sub');
    var phaseOneInput = $('#phase1RadioInput');
    var phaseTwoInput = $('#phase2RadioInput');
    var alertSpan = $('#alert-phase');
    if (metricInput.val() == "0.0") metricInput.val("");
    submitted = false;

    // Changes the metric type accordingly
    metricSelect.on('ready change', function () {
        $('#metricHeader').html(metricSelect.find('option:selected').val());
    });

    // This one just adds the same event listener to the 4 elements
    metricSelect.add(metricInput).add(phaseOneInput).add(phaseTwoInput)
        .on('ready change paste keyup', function () {
            metricInput.val(numberWithCommas(metricInput.val()));
            if (isNetworkUpgradeNeeded(phaseOneInput, metricSelect, metricInput)) alertSpan.prop('hidden', false);
            else alertSpan.prop('hidden', true);
    });

    $('#submit').on('click', function(){
        metricInput.val(metricInput.val().replace(".", ""));
    })

});

var isNetworkUpgradeNeeded = function(phaseOneInput, metricSelect, metricInput){
    // If phase is 1, and the selected metric is kWh and the input's value is bigger than 5, then upgrade is needed
    var kWhIsTooHigh =  metricSelect.find('option:selected').val() == 'kWh' && metricInput.val() >= 5;

    // If phase is 1, and the selected metric is Ft and the input's value is bigger than 18000, then upgrade is needed
    //TODO: Ask if it is really 18000, where it should warn about the phases
    var HufIsTooHigh =  metricSelect.find('option:selected').val() == 'Ft' && metricInput.val().replace(".", "") >= 18000;

    return phaseOneInput.is(':checked') && ( kWhIsTooHigh || HufIsTooHigh);
};
var numberWithCommas = function (x) {
    return x.toString().replace(".", "").replace(/\B(?=(\d{3})+(?!\d))/g, ".");
};
