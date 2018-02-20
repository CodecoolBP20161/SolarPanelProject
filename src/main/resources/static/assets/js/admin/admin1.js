$(document).ready(function () {

    var metricSelect = $('#stepOneSelect');
    var metricInput = $('#sub');
    var phaseOneInput = $('#phase1RadioInput');
    var phaseTwoInput = $('#phase2RadioInput');

    if (metricInput.val() == "0.0") {
        metricInput.val("");
    } else {
        (metricInput.val(formatPrice(metricInput.val())));
    }

    metricSelect.on('ready change', function () {
        $('#metricHeader').html(metricSelect.find('option:selected').val());
    });

    metricSelect.add(metricInput).add(phaseOneInput).add(phaseTwoInput)
        .on('ready change paste keyup', function () {
            var formattedInput = formatPrice(metricInput.val());

            if (isNumberInputFieldValueValid(metricInput.val())){
                metricInput.val(formattedInput);
            }

            isNetworkUpgradeNeededAdmin(phaseOneInput, metricSelect, metricInput);
        });

    $('#submit').on('click', function(){
        var inputValue = metricInput.val();
        metricInput.val(formatPrice(inputValue));
    });



    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
    var csrfToken = $("meta[name='_csrf']").attr("content");
    var headers = {};

    headers[csrfHeader] = csrfToken;

    var facebookData;
    var googleData;
    var suggestionData;

    $.ajax({
        url: "/admin/statistics",
        type: 'GET',
        dataType: "json",
        headers: headers,
        success: function (data) {

            for (var i=0; i<data.length; i++) {
               if (data[i].type === 1) {
                   facebookData = data[i].value;
               } else if (data[i].type === 2){
                   googleData = data[i].value;
               } else {
                   suggestionData = data[i].value;
               }
            }
        }
    });


    google.charts.load('current', {'packages':['corechart']});
    google.charts.setOnLoadCallback(drawChart);

    function drawChart() {

        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Nézettség');
        data.addColumn('number', 'Érték');
        data.addRows([
            ['Facebook', facebookData],
            ['Google', googleData],
            ['Ajánlás', suggestionData]

        ]);

        var options = {
            title: 'Látogatottság megoszlása',
            sliceVisibilityThreshold: .2
        };

        var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
        chart.draw(data, options);
    }




});

var isNetworkUpgradeNeededAdmin = function(phaseOneInput, metricSelect, metricInput){
    var alertSpan = $('#alert-phase');

    if (phaseOneInput.val() == "1"){
        var metric = metricSelect.find('option:selected').val();
        var value =unFormatPrice(metricInput.val());

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
