$(document).ready(function() {
        var metricSelect = $('#stepOneSelect');
        metricSelect.on('change', function () {
            $('#metricHeader').html(metricSelect.find('option:selected').val());
        })
    }
);

