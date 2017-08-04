$(document).ready(function () {

    // FORM PAGE 1

    var metricSelect = $('#stepOneSelect');
    var metricInput = $('#sub');
    var phaseOneInput = $('#phase1RadioInput');
    var phaseTwoInput = $('#phase2RadioInput');
    var alertSpan = $('#alert-phase');
    if (metricInput.val() == "0.0") metricInput.val("");

    // Changes the metric type accordingly
    metricSelect.on('change', function () {
        $('#metricHeader').html(metricSelect.find('option:selected').val());
    });
    metricSelect
        .add(metricInput)
        .add(phaseOneInput)
        .add(phaseTwoInput).on('change paste keyup', function () {
        // If phase is 1, and the selected metric is kWh and the inputs value is bigger than 5, then the message should show
        if (phaseOneInput.is(':checked') && metricSelect.find('option:selected').val() == 'kWh' && metricInput.val() >= 5) {
            alertSpan.prop('hidden', false);
        }
        //TODO: write condition for (havi villanyszámla alapján)
        // } else if (write your fukin shit here, and uncomment){
        //     alertSpan.prop('hidden', false);
        // }
        else {
            alertSpan.prop('hidden', true);
        }
    });


//____________________________________________________________________________________________________


// FORM PAGE 2

    var inverterInput = $('#inverterInput');
    var panelInput = $('#panelInput');
    if (inverterInput.val() == undefined) {
        inverterInput.val('initial');
        panelInput.val('initial');
    }

    var formSubmitbutton = $('#submitPanelAndInverter');
    var selectedInverter = null;
    var selectedPanel = null;

    // Toggles class on clicked inverter and set hidden input's value to clicked inverters id
    $('.inverter').on('click', function (event) {
        if (selectedInverter) {
            selectedInverter.toggleClass('active');
        }
        selectedInverter = $(event.target.closest('div[class^="testimonial-block cyan-background inverter"]'));
        selectedInverter.toggleClass('active');
        inverterInput.val(selectedInverter.attr('data')).change();
    });

    // Toggles class on clicked panel and set hidden input's value to clicked panel id
    $('.panel').on('click', function (event) {

        if (selectedPanel) {
            selectedPanel.toggleClass('active');
        }
        selectedPanel = $(event.target.closest('div[class^="testimonial-block cyan-background panel"]'));
        selectedPanel.toggleClass('active');
        panelInput.val(selectedPanel.attr('data')).change()
    });

    // When an input is changed(panel or inverter), it checks whether there are still
    // input with initial value (if the other one is chosen as well),
    // and sets the buttons disabled property accordingly
    inverterInput.add(panelInput).on('change', function () {
        if (inverterInput.val() !== 'initial' && panelInput.val() !== 'initial')
            formSubmitbutton.prop('disabled', false);
    });

//_______________________________________________________________________________________________


    // FORM 3
    $('#submitEmail').on('click', function (event) {
        event.preventDefault();

        var req = new XMLHttpRequest();
        // "http://52.15.84.238:1350/api/printpdf1"
        req.open("POST", "http://52.15.84.238:1350/api/getofferpdf", true);
        req.responseType = 'arraybuffer';
        var data = JSON.stringify({
            "id": 123321,

            "items" :
                [
                    {
                        "name": "270W-os Amerisolar polikristályos napelem",
                        "price": 10000,
                        "quantity": 10,
                        "subtotal": 100000,
                        "description": "Legjobb termék"
                    },
                    {
                        "name": "270W-os Heckert Solar polikristályos napelem",
                        "price": 10000,
                        "quantity": 10,
                        "subtotal": 100000,
                        "description": "Legjobb termék"
                    },
                    {
                        "name": "270W-os Heckert Solar polikristályos napelem",
                        "price": 10000,
                        "quantity": 10,
                        "subtotal": 100000,
                        "description": "Legjobb termék"
                    },
                    {
                        "name": "270W-os Heckert Solar polikristályos napelem",
                        "price": 10000,
                        "quantity": 10,
                        "subtotal": 100000,
                        "description": "Legjobb termék"
                    },
                    {
                        "name": "270W-os Heckert Solar polikristályos napelem",
                        "price": 10000,
                        "quantity": 10,
                        "subtotal": 100000,
                        "description": "Legjobb termék"
                    },
                    {
                        "name": "Solaredge SE2200-ER-01+wifi",
                        "price": 10000,
                        "quantity": 10,
                        "subtotal": 100000,
                        "description": "Legjobb termék"
                    },
                    {
                        "name": "Growatt 5000MTL-S inverter",
                        "price": 10000,
                        "quantity": 10,
                        "subtotal": 100000,
                        "description": "Legjobb termék"
                    }
                ],

            "services" :
                [
                    {
                        "name": "Service1",
                        "price": 10000,
                        "quantity": 10,
                        "subtotal": 100000,
                        "description": "Legjobb termék"
                    },
                    {
                        "name": "Service2",
                        "price": 10000,
                        "quantity": 10,
                        "subtotal": 100000,
                        "description": "Legjobb termék"
                    },
                    {
                        "name": "Service3",
                        "price": 10000,
                        "quantity": 10,
                        "subtotal": 100000,
                        "description": "Legjobb termék"
                    },
                    {
                        "name": "Service4",
                        "price": 10000,
                        "quantity": 10,
                        "subtotal": 100000,
                        "description": "Legjobb termék"
                    },
                    {
                        "name": "Service5",
                        "price": 10000,
                        "quantity": 10,
                        "subtotal": 100000,
                        "description": "Legjobb termék"
                    }

                ],

            "isNetworkUpgradeNeeded": true,

            "taxRate": 27,

            "netTotal": 9999999,
            "grossTotal": 124343

        });
        req.setRequestHeader("Content-type", "application/json");

        req.onload = function () {
            var blob = new Blob([req.response], {type: "application/pdf"});
            console.log(blob);
            var fileURL = window.URL.createObjectURL(blob);
            // window.open(fileURL, 'SunnyHome_ajanlat_id123123');

            // If you want to download that shit, uncomment this
            var link = document.createElement('a');
            link.href = fileURL;
            link.download="NaposOldal._ajanlat_id" + ".pdf";
            link.click();
        };

        req.send(data);
    });
});




